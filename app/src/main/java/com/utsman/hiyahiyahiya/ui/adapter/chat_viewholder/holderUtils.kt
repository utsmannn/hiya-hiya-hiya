package com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.utils.messageStatusBody
import com.utsman.hiyahiyahiya.utils.Broadcast
import kotlinx.coroutines.GlobalScope

fun ImageView.generateStatus(itemChatItem: RowChatItem.ChatItem) {
    when (itemChatItem.localChatStatus) {
        LocalChatStatus.NONE -> visibility = View.GONE
        LocalChatStatus.SEND -> {
            visibility = View.VISIBLE
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_done_24))
        }
        LocalChatStatus.RECEIVED -> {
            visibility = View.VISIBLE
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_done_all_24))
        }
        LocalChatStatus.READ -> {
            visibility = View.VISIBLE
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_done_all_24))
            setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        }
    }
}

fun RowChatItem.ChatItem.postRead() {
    if (localChatStatus != LocalChatStatus.READ) {
        val localStatusBody = messageStatusBody {
            this.chatId = id
            this.ownerId = from ?: ""
            this.localStatus = LocalChatStatus.READ
        }
        Broadcast.with(GlobalScope).post("read_item", localStatusBody)
    }
}

fun Context.openLink(url: String?) {
    Intent(Intent.ACTION_VIEW).apply {
        data = url?.toUri()
        startActivity(this)
    }
}