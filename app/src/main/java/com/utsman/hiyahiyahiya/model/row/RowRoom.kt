package com.utsman.hiyahiyahiya.model.row

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import kotlinx.android.parcel.Parcelize

enum class RowRoomType {
    ITEM, EMPTY
}

sealed class RowRoom(val rowRoomType: RowRoomType) {
    @Parcelize
    data class RoomItem(
        var id: String = "ariel-tatum",
        var chatsId: List<String> = emptyList(),
        var membersId: List<String> = emptyList(),
        var lastDate: Long? = 0L,
        var titleRoom: String? = "",
        var subtitleRoom: String? = "",
        var imageRoom: String? = "",
        @SerializedName("local_chat_status")
        var localChatStatus: LocalChatStatus = LocalChatStatus.NONE,
        @SerializedName("image_badge")
        var imageBadge: Boolean = false
    ) : Parcelable, RowRoom(RowRoomType.ITEM)

    data class Empty(
        var text: String = "Empty"
    ) : RowRoom(RowRoomType.EMPTY)
}