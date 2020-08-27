package com.utsman.hiyahiyahiya.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.inlineactivityresult.startActivityForResult
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.ui.CameraResultActivity
import com.utsman.hiyahiyahiya.ui.adapter.PhotosPagedAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.PhotosViewModel
import com.utsman.hiyahiyahiya.utils.Broadcast
import com.utsman.hiyahiyahiya.utils.bottom_sheet.BottomSheetBehaviorRecyclerManager
import com.utsman.hiyahiyahiya.utils.bottom_sheet.BottomSheetBehaviorRv
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.saveImage
import com.utsman.hiyahiyahiya.utils.withPermissions
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.bottom_sheet_photos.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : Fragment() {

    private val photosViewModel: PhotosViewModel by viewModel()

    private val photoAdapter1: PhotosPagedAdapter by inject()
    private val photoAdapter2: PhotosPagedAdapter by inject()

    private var room: RowRoom.RoomItem? = null
    private var toMember: String? = null
    private var intentType: String? = null

    private var fromPager = false

    companion object {
        fun instance(room: RowRoom.RoomItem?, toMember: String?, intentType: String) : CameraFragment {
            val fragment = CameraFragment()
            val bundle = bundleOf(
                "room" to room,
                "to_member" to toMember,
                "intent_type" to intentType
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            room = getParcelable("room")
            toMember = getString("to_member")
            intentType = getString("intent_type")
        }

        setupView()
    }

    fun isFromPager(fromPager: Boolean) {
        this.fromPager = fromPager
    }

    private fun setupView() {
        camera_view.setLifecycleOwner(this)

        bg_view.alpha = 0f
        tx_all_photos.alpha = 0f
        rv_photo2.alpha = 0f

        container_control.visibility = View.VISIBLE
        bg_view.visibility = View.GONE
        tx_all_photos.visibility = View.GONE
        rv_photo2.visibility = View.GONE

        setupBottomSheet()
        lifecycleScope.launch {
            delay(1000)
            CoroutineScope(Dispatchers.Main).launch {
                setupRecyclerViewPhotos()
            }
        }

        val hasFlash = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (!hasFlash) btn_flash.alpha = 0.4f
        btn_flash.click {
            if (hasFlash) {
                setupFlash()
            }
        }

        btn_flip.click {
            setupFacing()
        }

        btn_take_picture.click {
            it.animate()
                .scaleY(1.5f)
                .scaleX(1.5f)
                .setDuration(300)
                .withEndAction {
                    it.animate()
                        .scaleY(1f)
                        .scaleX(1f)
                        .withEndAction {
                            takingPicture()
                        }
                        .start()
                }
                .start()
        }

        camera_view.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                result.data.saveImage(false) { file ->
                    val path = file.absolutePath
                    intentToResult(path)
                }
            }
        })
    }

    private fun setupFacing() {
        if (camera_view.facing == Facing.BACK) {
            camera_view.facing = Facing.FRONT
        } else {
            camera_view.facing = Facing.BACK
        }
    }

    private fun setupFlash() {
        if (camera_view.flash == Flash.OFF) {
            camera_view.flash = Flash.ON
            btn_flash.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_flash_on_24))
        } else if (camera_view.flash == Flash.ON) {
            camera_view.flash = Flash.OFF
            btn_flash.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_flash_off_24))
        }
    }

    private fun takingPicture() {
        camera_view.takePicture()
    }

    private fun intentToResult(path: String) {
        val intent = Intent(requireContext(), CameraResultActivity::class.java).apply {
            putExtra("image_path", path)
            putExtra("room", room)
            putExtra("to", toMember)
            putExtra("intent_type", intentType)
        }

        startActivityForResult(intent) { success, data ->
            if (success) {
                if (!fromPager) {
                    requireActivity().finish()
                } else {
                    Broadcast.with(GlobalScope).post("direct_main_chat")
                }
            }
        }
    }

    private fun setupBottomSheet() {
        val bottomSheet = BottomSheetBehaviorRv.from(parent_bottom_sheet)
        bottomSheet.setBottomSheetCallback(object : BottomSheetBehaviorRv.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bg_view.alpha = slideOffset
                tx_all_photos.alpha = slideOffset
                rv_photo2.alpha = slideOffset

                val reverseOffset = 1 - slideOffset
                rv_photo1.alpha = reverseOffset

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
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })

        val manager = BottomSheetBehaviorRecyclerManager(parent_layout_photo, bottomSheet, parent_bottom_sheet)
        manager.apply {
            addControl(rv_photo1)
            addControl(rv_photo2)
            create()
        }
    }

    private fun setupRecyclerViewPhotos() {
        val linearLayout = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val gridLayout = GridLayoutManager(requireContext(), 3)

        photoAdapter1.setType(0)
        photoAdapter2.setType(1)

        rv_photo1.run {
            layoutManager = linearLayout
            adapter = photoAdapter1

            photoAdapter1.onClick {
                if (alpha == 1f) {
                    intentToResult(it.uri)
                }
            }
        }

        rv_photo2.run {
            layoutManager = gridLayout
            adapter = photoAdapter2

            photoAdapter2.onClick {
                if (alpha == 1f) {
                    intentToResult(it.uri)
                }
            }
        }

        photosViewModel.photos()
        photosViewModel.data.observe(viewLifecycleOwner, Observer {
            photoAdapter1.submitList(it)
            photoAdapter2.submitList(it)
        })
    }
}