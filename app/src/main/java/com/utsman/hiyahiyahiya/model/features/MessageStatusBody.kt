package com.utsman.hiyahiyahiya.model.features

import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus

data class MessageStatusBody(
    @SerializedName("chat_id")
    var chatId: String = "",
    @SerializedName("owner_id")
    var ownerId: String = "",
    @SerializedName("local_status")
    var localStatus: LocalChatStatus = LocalChatStatus.SEND
)