package com.utsman.hiyahiyahiya.model

import android.os.Parcelable
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import kotlinx.android.parcel.Parcelize

sealed class RowChat {
    @Parcelize
    data class ChatRoom(
        var id: String = "ariel-tatum",
        var chatsId: List<String> = emptyList(),
        var membersId: List<String> = emptyList(),
        var lastDate: Long = 0L
    ): Parcelable, RowChat()
}