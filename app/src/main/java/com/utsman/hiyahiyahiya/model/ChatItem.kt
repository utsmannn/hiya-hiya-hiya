package com.utsman.hiyahiyahiya.model

import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.data.UserPref

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
        var attachment: List<MessageAttachment> = emptyList()
    ): RowChatItem(if (from == UserPref.getUserId()) RowChatType.ME else RowChatType.OTHER)

    data class Empty(
        var text: String = "Empty"
    ) : RowChatItem(RowChatType.EMPTY)
}