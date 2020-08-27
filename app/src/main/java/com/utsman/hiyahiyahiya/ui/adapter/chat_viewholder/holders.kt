package com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.perfomer.blitz.setTimeAgo
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.model.features.UrlAttachment
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.item_chat_me.view.img_send_indicator
import kotlinx.android.synthetic.main.item_chat_me.view.tx_date_me
import kotlinx.android.synthetic.main.item_chat_me.view.tx_message_me
import kotlinx.android.synthetic.main.item_chat_me_images.view.*
import kotlinx.android.synthetic.main.item_chat_me_url.view.*
import kotlinx.android.synthetic.main.item_chat_other.view.tx_date_other
import kotlinx.android.synthetic.main.item_chat_other.view.tx_message_other
import kotlinx.android.synthetic.main.item_chat_other_image.view.*
import kotlinx.android.synthetic.main.item_chat_other_url.view.*
import kotlinx.android.synthetic.main.item_divider_chat.view.*
import kotlinx.android.synthetic.main.item_list_empty.view.*

class ItemChatMeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_date_me.setTimeAgo(itemChatItem.time ?: 0L)
        img_send_indicator.generateStatus(itemChatItem)
    }
}

class ItemChatMeImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_date_me.setTimeAgo(itemChatItem.time ?: 0L)
        img_send_indicator.generateStatus(itemChatItem)

        val url = if (itemChatItem.imageAttachment.first().imageBBSimple?.url != null) {
            itemChatItem.imageAttachment.first().imageBBSimple?.url
        } else {
            itemChatItem.imageAttachment.first().imageBBSimple?.urlLarge
        }

        Picasso.get().load(url).resize(300, 0).into(img_message_me)

        if (itemChatItem.message?.isEmpty() == true) tx_message_me.visibility = View.GONE

    }
}

class ItemChatMeUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_date_me.setTimeAgo(itemChatItem.time ?: 0L)
        img_send_indicator.generateStatus(itemChatItem)

        itemChatItem.urlAttachment?.let { url ->
            if (url == UrlAttachment()) card_url_me.visibility = View.GONE
            if (url.image.isNullOrEmpty()) {
                img_url_me.visibility = View.GONE
            } else {
                Picasso.get().load(url.image).resize(100, 0).into(img_url_me)
            }

            tx_title_url_me.text = url.title
            tx_subtitle_url_me.text = url.subtitle
            tx_url_me.text = url.url

            card_url_me.click {
                context?.openLink(url.url)
            }
        }

    }
}

class ItemChatOtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_date_other.setTimeAgo(itemChatItem.time ?: 0L)
    }
}

class ItemChatOtherImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_date_other.setTimeAgo(itemChatItem.time ?: 0L)

        val url = if (itemChatItem.imageAttachment.first().imageBBSimple?.url != null) {
            itemChatItem.imageAttachment.first().imageBBSimple?.url
        } else {
            itemChatItem.imageAttachment.first().imageBBSimple?.urlLarge
        }

        Picasso.get().load(url).resize(300, 0).into(img_message_other)

        if (itemChatItem.message?.isEmpty() == true) tx_message_other.visibility = View.GONE

    }
}

class ItemChatOtherUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_date_other.setTimeAgo(itemChatItem.time ?: 0L)

        itemChatItem.urlAttachment?.let { url ->
            if (url == UrlAttachment()) card_url_other.visibility = View.GONE
            if (url.image.isNullOrEmpty()) {
                img_url_other.visibility = View.GONE
            } else {
                Picasso.get().load(url.image).resize(100, 0).into(img_url_other)
            }

            tx_title_url_other.text = url.title
            tx_subtitle_url_other.text = url.subtitle
            tx_url_other.text = url.url

            card_url_other.click {
                context?.openLink(url.url)
            }
        }
    }
}

class ItemChatDivider(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_divider_date.text = itemChatItem.message
    }
}

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(empty: RowChatItem.Empty) = itemView.run {
        tx_empty_message.text = empty.text
    }
}