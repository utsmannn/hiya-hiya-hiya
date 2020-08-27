package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowChatType
import com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ChatDiffCallback(
        private val oldList: List<RowChatItem>,
        private val newList: List<RowChatItem>
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

    private val chatList: MutableList<RowChatItem> = mutableListOf()

    fun addChat(chats: List<RowChatItem>) = apply {
        val diffCallback = ChatDiffCallback(chatList, chats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        chatList.clear()
        chatList.addAll(chats)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (RowChatType.values()[viewType]) {
            RowChatType.ME -> ItemChatMeViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_me, parent, false)
            )
            RowChatType.ME_IMAGE -> ItemChatMeImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_me_images, parent, false)
            )
            RowChatType.ME_URL -> ItemChatMeUrlViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_me_url, parent, false)
            )
            RowChatType.OTHER -> ItemChatOtherViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_other, parent, false)
            )
            RowChatType.OTHER_IMAGE -> ItemChatOtherImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_other_image, parent, false)
            )
            RowChatType.OTHER_URL -> ItemChatOtherUrlViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_other_url, parent, false)
            )
            else -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = chatList.size
    override fun getItemViewType(position: Int): Int = chatList[position].rowChatType.ordinal
    override fun getItemId(position: Int): Long = chatList[position].identifier ?: 0L

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = chatList[position]) {
            is RowChatItem.ChatItem -> {
                when (item.rowChatType) {
                    RowChatType.ME -> (holder as ItemChatMeViewHolder).bind(item)
                    RowChatType.ME_IMAGE -> (holder as ItemChatMeImageViewHolder).bind(item)
                    RowChatType.ME_URL -> (holder as ItemChatMeUrlViewHolder).bind(item)
                    RowChatType.OTHER_IMAGE -> (holder as ItemChatOtherImageViewHolder).bind(item)
                    RowChatType.OTHER_URL -> (holder as ItemChatOtherUrlViewHolder).bind(item)
                    else -> (holder as ItemChatOtherViewHolder).bind(item)
                }
            }
            is RowChatItem.Empty -> (holder as EmptyViewHolder).bind(item)
        }
    }
}