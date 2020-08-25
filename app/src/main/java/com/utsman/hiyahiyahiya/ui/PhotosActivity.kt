package com.utsman.hiyahiyahiya.ui

import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowImage
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.ui.adapter.PhotoAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.PhotosViewModel
import com.utsman.hiyahiyahiya.utils.*
import kotlinx.android.synthetic.main.activity_photos.*
import kotlinx.android.synthetic.main.bottom_sheet_photos.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class PhotosActivity : AppCompatActivity() {

    private val photosViewModel: PhotosViewModel by viewModel()
    private val photoAdapter1: PhotoAdapter by inject()
    private val photoAdapter2: PhotoAdapter by inject()

    private val FRONT_CAMERA = 1
    private val BACK_CAMERA = 0
    private var flashOn = true

    private val liveCameraFacing: MutableLiveData<Int> = MutableLiveData(BACK_CAMERA)
    private val room by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val toMember by lazy { intent.getStringExtra("to") }

    private var camera: Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)

        bg_view.alpha = 0f
        tx_all_photos.alpha = 0f
        rv_photo2.alpha = 0f

        bg_view.visibility = View.GONE
        tx_all_photos.visibility = View.GONE
        rv_photo2.visibility = View.GONE

        setupSurface()
        setupBottomSheet()
        lifecycleScope.launch {
            delay(1000)
            runOnUiThread {
                setupRecyclerViewPhotos()
            }
        }

        val hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        btn_flash.click {
            if (hasFlash) {
                setupFlash(flashOn)
            }
        }

        btn_flip.click {
            liveCameraFacing.let {
                if (it.value == BACK_CAMERA) it.postValue(FRONT_CAMERA)
                else it.postValue(BACK_CAMERA)
            }
        }

        btn_take_picture.click {
            takingPicture()
        }
    }

    private fun takingPicture() {
        if (camera != null) {
            camera?.takePicture(null, null, Camera.PictureCallback { data, camera ->
                logi("bytes raw ---> $data")
                data.saveImage(validation = liveCameraFacing.value == FRONT_CAMERA) { file ->
                    val path = file.absolutePath
                    intentToResult(path)
                }
            })
        }
    }

    private fun intentToResult(path: String) {
        intentTo(ImageResultActivity::class.java) {
            putExtra("image_path", path)
            putExtra("room", room)
            putExtra("to", toMember)
        }
        finish()
    }


    private fun setupBottomSheet() {
        val bottomSheet = BottomSheetBehavior.from(parent_bottom_sheet)
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bg_view.alpha = slideOffset
                tx_all_photos.alpha = slideOffset
                rv_photo2.alpha = slideOffset

                if (slideOffset > 0) {
                    container_control.visibility = View.GONE
                    bg_view.visibility = View.VISIBLE
                    tx_all_photos.visibility = View.VISIBLE
                    rv_photo2.visibility = View.VISIBLE
                } else {
                    container_control.visibility = View.VISIBLE
                    bg_view.visibility = View.GONE
                    tx_all_photos.visibility = View.GONE
                    rv_photo2.visibility = View.GONE
                }

                scroll_photo.isVerticalScrollBarEnabled = slideOffset == 1f
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
    }

    private fun setupRecyclerViewPhotos() {
        val linearLayout = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val gridLayout = GridLayoutManager(this, 3)

        rv_photo1.run {
            layoutManager = linearLayout
            adapter = photoAdapter1
        }

        rv_photo2.run {
            layoutManager = gridLayout
            adapter = photoAdapter2
        }

        GlobalScope.launch {
            photosViewModel.allPhotos().collect { p ->
                runOnUiThread {
                    photoAdapter1.addPhoto(RowImage.Item1(p.uri))
                    photoAdapter2.addPhoto(RowImage.Item2(p.uri))
                }
            }
        }

        photoAdapter1.onClick {
            intentToResult(it)
        }

        photoAdapter2.onClick {
            intentToResult(it)
        }
    }

    private fun setupFlash(on: Boolean = false) {
        camera?.run {
            stopPreview()
            val param = parameters.apply {
                flashMode = if (on) {
                    toast("Flash on")
                    flashOn = false
                    btn_flash.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PhotosActivity,
                            R.drawable.ic_baseline_flash_on_24
                        )
                    )
                    Camera.Parameters.FLASH_MODE_ON
                } else {
                    toast("Flash off")
                    flashOn = true
                    btn_flash.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PhotosActivity,
                            R.drawable.ic_baseline_flash_off_24
                        )
                    )
                    Camera.Parameters.FLASH_MODE_OFF
                }
            }
            parameters = param
            startPreview()
        }
    }

    private fun setupSurface() {
        val surfaceHolder = surface_camera.holder
        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                resetCamera(holder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                releaseCamera()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                liveCameraFacing.observe(this@PhotosActivity, Observer {
                    releaseCamera()
                    btn_flash.visibility = if (it == FRONT_CAMERA) {
                        View.INVISIBLE
                    } else {
                        View.VISIBLE
                    }
                    startCamera(holder, it)
                })

                liveCameraFacing.postValue(BACK_CAMERA)
            }

        })
    }

    private fun releaseCamera() {
        if (camera != null) {
            camera?.run {
                stopPreview()
                release()
            }
            camera = null
        }
    }

    private fun resetCamera(holder: SurfaceHolder?) {
        if (holder != null) {
            camera?.run {
                stopPreview()
                try {
                    setPreviewDisplay(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                startPreview()
            }
        } else {
            return
        }
    }

    private fun startCamera(holder: SurfaceHolder?, facing: Int) {
        if (holder != null) {
            camera = Camera.open(facing).apply {
                setDisplayOrientation(90)
                this.parameters.let {
                    it.jpegQuality = 100
                    it.pictureFormat = ImageFormat.JPEG

                    val sizes = it.supportedPictureSizes
                    var size = sizes[0]
                    for (i in sizes.indices) {
                        if (sizes[i].width > size.width) size = sizes[i]
                    }
                    it.setPictureSize(size.width, size.height)
                }
            }
            try {
                camera?.run {
                    setPreviewDisplay(holder)
                    startPreview()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            return
        }
    }
}