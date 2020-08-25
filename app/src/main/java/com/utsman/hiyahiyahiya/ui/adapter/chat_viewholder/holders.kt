package com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.features.UrlAttachment
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.item_chat_me.view.img_send_indicator
import kotlinx.android.synthetic.main.item_chat_me.view.tx_message_me
import kotlinx.android.synthetic.main.item_chat_me.view.tx_status_me
import kotlinx.android.synthetic.main.item_chat_me_images.view.*
import kotlinx.android.synthetic.main.item_chat_me_url.view.*
import kotlinx.android.synthetic.main.item_chat_other.view.tx_message_other
import kotlinx.android.synthetic.main.item_chat_other.view.tx_status_other
import kotlinx.android.synthetic.main.item_chat_other_image.view.*
import kotlinx.android.synthetic.main.item_chat_other_url.view.*
import kotlinx.android.synthetic.main.item_list_empty.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemChatMeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_status_me.text = itemChatItem.stringTime()

        when (itemChatItem.localChatStatus) {
            LocalChatStatus.NONE -> img_send_indicator.visibility = View.GONE
            LocalChatStatus.SEND -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_done_24))
            }
            LocalChatStatus.RECEIVED -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_done_all_24))
            }
        }
    }
}

class ItemChatMeImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_status_me.text = itemChatItem.stringTime()
        Picasso.get().load(itemChatItem.imageAttachment.first().imageBBSimple?.url).resize(300, 0).into(img_message_me)

        if (itemChatItem.message?.isEmpty() == true) tx_message_me.visibility = View.GONE

        when (itemChatItem.localChatStatus) {
            LocalChatStatus.NONE -> img_send_indicator.visibility = View.GONE
            LocalChatStatus.SEND -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_done_24))
            }
            LocalChatStatus.RECEIVED -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_done_all_24))
            }
        }
    }
}

class ItemChatMeUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_me.text = itemChatItem.message
        tx_status_me.text = itemChatItem.stringTime()

        CoroutineScope(Dispatchers.IO).launch {
            itemChatItem.urlAttachment?.let { url ->
                CoroutineScope(Dispatchers.Main).launch {
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

        when (itemChatItem.localChatStatus) {
            LocalChatStatus.NONE -> img_send_indicator.visibility = View.GONE
            LocalChatStatus.SEND -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_done_24))
            }
            LocalChatStatus.RECEIVED -> {
                img_send_indicator.visibility = View.VISIBLE
                img_send_indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_done_all_24))
            }
        }
    }
}

class ItemChatOtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_status_other.text = itemChatItem.stringTime()
    }
}

class ItemChatOtherImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_status_other.text = itemChatItem.stringTime()
        Picasso.get().load(itemChatItem.imageAttachment.first().imageBBSimple?.url).resize(300, 0).into(img_message_other)

        if (itemChatItem.message?.isEmpty() == true) tx_message_other.visibility = View.GONE

    }
}

class ItemChatOtherUrlViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
        tx_message_other.text = itemChatItem.message
        tx_status_other.text = itemChatItem.stringTime()

        CoroutineScope(Dispatchers.IO).launch {
            itemChatItem.urlAttachment?.let { url ->
                CoroutineScope(Dispatchers.Main).launch {
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
    }
}

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(empty: RowChatItem.Empty) = itemView.run {
        tx_empty_message.text = empty.text
    }
}

fun Context.openLink(url: String?) {
    Intent(Intent.ACTION_VIEW).apply {
        data = url?.toUri()
        startActivity(this)
    }
}