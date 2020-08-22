package com.utsman.hiyahiyahiya.model

import android.os.Parcelable
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
        var imageRoom: String? = ""
    ): Parcelable, RowRoom(RowRoomType.ITEM)

    data class Empty(
        var text: String = "Empty"
    ) : RowRoom(RowRoomType.EMPTY)
}