package com.utsman.hiyahiyahiya.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.inlineactivityresult.startActivityForResult
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.model.types.TypeCamera
import com.utsman.hiyahiyahiya.ui.adapter.PhotosPagedAdapter
import com.utsman.hiyahiyahiya.ui.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.activity_gallery.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : AppCompatActivity() {

    private val photoAdapter: PhotosPagedAdapter by inject()
    private val photoViewModel: PhotosViewModel by viewModel()

    private val room by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val to by lazy { intent.getStringExtra("to") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val gridLayout = GridLayoutManager(this, 3)
        rv_gallery.apply {
            layoutManager = gridLayout
            adapter = photoAdapter.apply {
                setLayout(R.layout.item_photo_2)
                onClick {
                    intentToResult(it.uri)
                }
            }
        }

        photoViewModel.photos()
        photoViewModel.data.observe(this, Observer {
            photoAdapter.submitList(it)
        })
    }

    private fun intentToResult(path: String) {
        val intent = Intent(this, CameraResultActivity::class.java).apply {
            putExtra("image_path", path)
            putExtra("room", room)
            putExtra("to", to)
            putExtra("intent_type", TypeCamera.ATTACHMENT.name)
        }

        startActivityForResult(intent) { success, _ ->
            if (success) {
                finish()
            }
        }
    }
}