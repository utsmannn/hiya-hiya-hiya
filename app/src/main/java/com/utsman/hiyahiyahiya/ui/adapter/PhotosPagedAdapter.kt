package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.utils.click
import java.io.File

class PhotosPagedAdapter : PagedListAdapter<PhotoLocal, PhotosPagedAdapter.PhotoViewHolder>(diffCallback) {

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(photoLocal: PhotoLocal) = itemView.run {
            val imgPhoto = findViewById<ImageView>(R.id.img_photo)
            val file = File(photoLocal.uri)
            Picasso.get().load(file).resize(300, 0).into(imgPhoto)

            click {
                onPhotoClick?.invoke(photoLocal)
            }
        }
    }

    companion object {
        private val diffCallback = object :
            DiffUtil.ItemCallback<PhotoLocal>() {
            override fun areItemsTheSame(
                oldConcert: PhotoLocal,
                newConcert: PhotoLocal
            ) = oldConcert.date == newConcert.date

            override fun areContentsTheSame(
                oldConcert: PhotoLocal,
                newConcert: PhotoLocal
            ) = oldConcert == newConcert
        }
    }

    private var onPhotoClick: ((PhotoLocal) -> Unit)? = null

    fun onClick(onPhotoClick: ((PhotoLocal) -> Unit)? = null) {
        this.onPhotoClick = onPhotoClick
    }

    private var type = 0

    fun setType(type: Int = 0) {
        this.type = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            when (type) {
                0 -> LayoutInflater.from(parent.context).inflate(R.layout.item_photo_1, parent, false)
                else -> LayoutInflater.from(parent.context).inflate(R.layout.item_photo_2, parent, false)
            }
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (getItem(position) != null) holder.bind(getItem(position)!!)
    }
}