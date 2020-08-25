package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.utsman.hiyahiyahiya.model.features.ImageAttachment
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus
import com.utsman.hiyahiyahiya.model.features.UrlAttachment

@Entity(tableName = "local_chat")
data class LocalChat(
    @PrimaryKey
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
    var currentUser: LocalUser?,
    @SerializedName("local_chat_status")
    var localChatStatus: LocalChatStatus = LocalChatStatus.SEND
) {

    class Converter {
        private val gson = Gson()

        @TypeConverter
        fun fromListImageAttachment(value: List<ImageAttachment>): String {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toListImageAttachment(value: String): List<ImageAttachment> {
            return gson.fromJson(value, object : TypeToken<List<ImageAttachment>>() {
            }.type)
        }

        @TypeConverter
        fun fromUrlAttachment(value: UrlAttachment?): String? {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toUrlAttachment(value: String?): UrlAttachment? {
            return gson.fromJson(value, object : TypeToken<UrlAttachment>() {
            }.type)
        }

        @TypeConverter
        fun fromUser(value: LocalUser?): String? {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toUser(value: String?): LocalUser? {
            return gson.fromJson(value, object : TypeToken<LocalUser?>() {
            }.type)
        }

        @TypeConverter
        fun fromLocalStatus(value: LocalChatStatus): String {
            return value.name
        }

        @TypeConverter
        fun toLocalStatus(value: String): LocalChatStatus {
            return LocalChatStatus.valueOf(value)
        }
    }
}