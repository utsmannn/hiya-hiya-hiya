package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.RowImage
import com.utsman.hiyahiyahiya.model.RowImageType
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.toast
import java.io.File

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(uri: String) = itemView.run {
            val imgPhoto = findViewById<ImageView>(R.id.img_photo)
            val file = File(uri)
            Picasso.get().load(file).resize(300, 0).into(imgPhoto)

            click {
                context?.toast("click -> $it")
                onPhotoClick?.invoke(uri)
            }
        }
    }

    inner class ContactDiffCallback(
        private val oldList: List<RowImage>,
        private val newList: List<RowImage>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private val listPhoto: MutableList<RowImage> = mutableListOf()
    private var onPhotoClick: ((String) -> Unit)? = null

    fun onClick(onPhotoClick: ((String) -> Unit)? = null) {
        this.onPhotoClick = onPhotoClick
    }

    fun addPhoto(photo: RowImage) = apply {
        val diffCallback = ContactDiffCallback(listPhoto, listOf(photo))
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listPhoto.add(photo)
        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return when (RowImageType.values()[viewType]) {
            RowImageType.PHOTO_1 -> PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_1, parent, false)
            )
            RowImageType.PHOTO_2 -> PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_2, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = listPhoto.size
    override fun getItemViewType(position: Int): Int = listPhoto[position].rowImageType.ordinal

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        when (val item = listPhoto[position]) {
            is RowImage.Item1 -> holder.bind(item.uri)
            is RowImage.Item2 -> holder.bind(item.uri)
        }
    }
}