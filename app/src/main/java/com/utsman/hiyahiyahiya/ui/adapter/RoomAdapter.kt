package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.model.LocalChatStatus
import com.utsman.hiyahiyahiya.model.RowRoom
import com.utsman.hiyahiyahiya.model.RowRoomType
import com.utsman.hiyahiyahiya.model.toLocalRoom
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.load
import kotlinx.android.synthetic.main.item_list_empty.view.*
import kotlinx.android.synthetic.main.item_room.view.*

class RoomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ItemRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: RowRoom.RoomItem) = itemView.run {
            img_profile.load(item.imageRoom, isCircle = true)
            tx_title.text = item.titleRoom
            tx_subtitle.text = item.subtitleRoom

            when (item.localChatStatus) {
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

            click {
                onRoomClick?.invoke(item)
            }
        }
    }

    inner class EmptyRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: RowRoom.Empty) = itemView.run {
            tx_empty_message.text = item.text
        }
    }

    inner class ContactDiffCallback(
        private val oldList: List<RowRoom>,
        private val newList: List<RowRoom>
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

    private val roomList: MutableList<RowRoom> = mutableListOf()
    private var onRoomClick: ((RowRoom.RoomItem) -> Unit)? = null

    fun onClick(onRoomClick: ((RowRoom.RoomItem) -> Unit)? = null) {
        this.onRoomClick = onRoomClick
    }

    fun addChat(chats: List<RowRoom>) = apply {
        val diffCallback = ContactDiffCallback(roomList, chats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        roomList.clear()
        roomList.addAll(chats)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (RowRoomType.values()[viewType]) {
            RowRoomType.ITEM -> ItemRoomViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
            )
            RowRoomType.EMPTY -> EmptyRoomViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = roomList.size
    override fun getItemViewType(position: Int): Int = roomList[position].rowRoomType.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = roomList[position]) {
            is RowRoom.RoomItem -> (holder as ItemRoomViewHolder).bind(item)
            is RowRoom.Empty -> (holder as EmptyRoomViewHolder).bind(item)
        }
    }
}