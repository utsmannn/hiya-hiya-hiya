package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.RowChatItem
import com.utsman.hiyahiyahiya.model.RowChatType
import kotlinx.android.synthetic.main.item_chat_me.view.*
import kotlinx.android.synthetic.main.item_chat_other.view.*
import kotlinx.android.synthetic.main.item_list_empty.view.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ItemChatMeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
            tx_message_me.text = itemChatItem.message
            tx_status_me.text = itemChatItem.time.toString()
        }
    }

    inner class ItemChatOtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(itemChatItem: RowChatItem.ChatItem) = itemView.run {
            tx_message_other.text = itemChatItem.message
            tx_status_other.text = itemChatItem.time.toString()
        }
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(empty: RowChatItem.Empty) = itemView.run {
            tx_empty_message.text = empty.text
        }
    }

    inner class ContactDiffCallback(
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
        val diffCallback = ContactDiffCallback(chatList, chats)
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
            RowChatType.OTHER -> ItemChatOtherViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_other, parent, false)
            )
            RowChatType.EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = chatList.size
    override fun getItemViewType(position: Int): Int = chatList[position].rowChatType.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = chatList[position]) {
            is RowChatItem.ChatItem -> {
                when (item.rowChatType) {
                    RowChatType.ME -> (holder as ItemChatMeViewHolder).bind(item)
                    else -> (holder as ItemChatOtherViewHolder).bind(item)
                }
            }
            is RowChatItem.Empty -> (holder as EmptyViewHolder).bind(item)
        }
    }
}