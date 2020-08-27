package com.utsman.hiyahiyahiya.model.row

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.model.features.ImageAttachment
import com.utsman.hiyahiyahiya.model.features.UrlAttachment
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.utils.logi
import java.text.SimpleDateFormat

enum class RowChatType {
    ME, OTHER, EMPTY, ME_IMAGE, ME_URL, ME_ALL, OTHER_IMAGE, OTHER_URL, OTHER_ALL, DIVIDER
}

sealed class RowChatItem(var rowChatType: RowChatType, var identifier: Long?) {
    data class ChatItem(
        var id: String = "",
        var message: String? = "",
        var to: String? = "",
        var from: String? = "",
        var time: Long? = 0L,
        @SerializedName("room_id")
        var roomId: String? = "",
        @SerializedName("image_attachment")
        var imageAttachment: List<ImageAttachment> = emptyList(),
        @SerializedName("url_attachment")
        var urlAttachment: UrlAttachment? = null,
        @SerializedName("current_user")
        var currentUser: LocalUser? = LocalUser(),
        @SerializedName("local_chat_status")
        var localChatStatus: LocalChatStatus = LocalChatStatus.SEND,
        var divider: Boolean = false
    ) : RowChatItem(generateType(from, imageAttachment, urlAttachment, divider), time) {

        @SuppressLint("SimpleDateFormat")
        fun stringTime(): String {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(time)
        }
    }

    data class Empty(
        var text: String = "Empty"
    ) : RowChatItem(RowChatType.EMPTY, 0L)
}

fun generateType(from: String?, imageAttachment: List<ImageAttachment>, urlAttachment: UrlAttachment?, divider: Boolean): RowChatType {
    logi("aaaa url attch-> $urlAttachment")
    return when (divider) {
        true -> RowChatType.DIVIDER
        else -> {
            when {
                (from == UserPref.getUserId() && imageAttachment.isEmpty() && urlAttachment == null && !divider) -> RowChatType.ME
                (from == UserPref.getUserId() && imageAttachment.isNotEmpty() && urlAttachment == null && !divider) -> RowChatType.ME_IMAGE
                (from == UserPref.getUserId() && imageAttachment.isEmpty() && urlAttachment != null && !divider) -> RowChatType.ME_URL
                (from == UserPref.getUserId() && imageAttachment.isNotEmpty() && urlAttachment != null && !divider) -> RowChatType.ME_ALL

                (from != UserPref.getUserId() && imageAttachment.isEmpty() && urlAttachment == null && !divider) -> RowChatType.OTHER
                (from != UserPref.getUserId() && imageAttachment.isNotEmpty() && urlAttachment == null && !divider) -> RowChatType.OTHER_IMAGE
                (from != UserPref.getUserId() && imageAttachment.isEmpty() && urlAttachment != null && !divider) -> RowChatType.OTHER_URL
                (from != UserPref.getUserId() && imageAttachment.isNotEmpty() && urlAttachment != null && !divider) -> RowChatType.OTHER_ALL
                else -> RowChatType.EMPTY
            }
        }
    }
}