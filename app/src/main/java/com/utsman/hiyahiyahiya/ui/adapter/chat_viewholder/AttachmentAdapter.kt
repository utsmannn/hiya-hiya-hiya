package com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowAttachChat
import com.utsman.hiyahiyahiya.utils.CropCircleTransformation
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.item_attachment.view.*

class AttachmentAdapter : RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

    inner class AttachmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(rowAttachChat: RowAttachChat) = itemView.run {
            /*Picasso.get().load(rowAttachChat.bgResource).transform(CropCircleTransformation()).into(img_bg_attach)
            Picasso.get().load(R.drawable.bg_tint_drawable).transform(CropCircleTransformation()).into(img_bg_tint_attach)

            img_icon_attach.setImageDrawable(ContextCompat.getDrawable(context, rowAttachChat.iconResource))*/

            tx_attach_title.text = rowAttachChat.title
            click {
                onAttachClick?.invoke(rowAttachChat)
            }
        }
    }

    private val items: MutableList<RowAttachChat> = mutableListOf()
    private var onAttachClick: ((RowAttachChat) -> Unit)? = null

    fun addItems(list: List<RowAttachChat>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun onAttachClick(onAttachClick: ((RowAttachChat) -> Unit)? = null) {
        this.onAttachClick = onAttachClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return AttachmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_attachment, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(items[position])
    }
}