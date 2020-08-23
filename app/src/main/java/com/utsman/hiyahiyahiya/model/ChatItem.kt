package com.utsman.hiyahiyahiya.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import java.text.SimpleDateFormat

enum class RowChatType {
    ME, OTHER, EMPTY
}

sealed class RowChatItem(var rowChatType: RowChatType) {
    data class ChatItem(
        var id: String = "",
        var message: String? = "",
        var to: String? = "",
        var from: String? = "",
        var time: Long? = 0L,
        @SerializedName("room_id")
        var roomId: String? = "",
        var attachment: List<MessageAttachment> = emptyList(),
        @SerializedName("current_user")
        var currentUser: LocalUser? = LocalUser(),
        @SerializedName("local_chat_status")
        var localChatStatus: LocalChatStatus = LocalChatStatus.SEND
    ): RowChatItem(if (from == UserPref.getUserId()) RowChatType.ME else RowChatType.OTHER) {

        @SuppressLint("SimpleDateFormat")
        fun stringTime(): String {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(time)
        }
    }

    data class Empty(
        var text: String = "Empty"
    ) : RowChatItem(RowChatType.EMPTY)
}