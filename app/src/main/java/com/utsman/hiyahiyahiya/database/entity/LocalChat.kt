package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.utsman.hiyahiyahiya.model.LocalChatStatus
import com.utsman.hiyahiyahiya.model.MessageAttachment

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
    var attachment: List<MessageAttachment> = emptyList(),
    @SerializedName("current_user")
    var currentUser: LocalUser?,
    var localChatStatus: LocalChatStatus
) {

    class Converter {
        private val gson = Gson()

        @TypeConverter
        fun fromListAttachment(value: List<MessageAttachment>): String {
            return value.toString()
        }

        @TypeConverter
        fun toListAttachment(value: String): List<MessageAttachment> {
            return gson.fromJson(value, object : TypeToken<List<MessageAttachment>>() {
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
    }
}