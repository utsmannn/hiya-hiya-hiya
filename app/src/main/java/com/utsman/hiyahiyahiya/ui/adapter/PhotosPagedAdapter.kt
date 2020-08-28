package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.model.features.PhotoType
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.android.synthetic.main.item_divider_photo.view.*
import java.io.File

class PhotosPagedAdapter : PagedListAdapter<PhotoLocal, RecyclerView.ViewHolder>(diffCallback) {

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

    inner class PhotoDivider(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(photoLocal: PhotoLocal) = itemView.run {
            tx_divider_date.text = photoLocal.dividerString
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

    private var mainLayout: Int = R.layout.item_photo_1

    fun setLayout(@LayoutRes layoutRes: Int) {
        this.mainLayout = layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (PhotoType.values()[viewType]) {
            PhotoType.ITEM -> PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(mainLayout, parent, false)
            )
            else -> {
                if (mainLayout == R.layout.item_photo_1) {
                    PhotoViewHolder(
                        LayoutInflater.from(parent.context).inflate(R.layout.item_photo_1, parent, false)
                    )
                } else {
                    PhotoDivider(
                        LayoutInflater.from(parent.context).inflate(R.layout.item_divider_photo, parent, false)
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position)?.photoType?.ordinal ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position) != null) {
            when (getItem(position)?.photoType) {
                PhotoType.ITEM -> (holder as PhotoViewHolder).bind(getItem(position)!!)
                else -> {
                    if (mainLayout == R.layout.item_photo_1) (holder as PhotoViewHolder).bind(getItem(position)!!)
                    else (holder as PhotoDivider).bind(getItem(position)!!)
                }
            }

        }
    }

    fun fixGridSpan() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            logi("view type --> ${getItemViewType(position)}")
            return when (getItemViewType(position)) {
                PhotoType.ITEM.ordinal -> 1
                PhotoType.DIVIDER.ordinal -> 3
                else -> 1
            }
        }
    }
}