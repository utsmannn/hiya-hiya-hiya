package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.utsman.hiyahiyahiya.model.types.LocalChatStatus

@Entity(tableName = "local_room")
data class LocalRoom(
    @PrimaryKey
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
) {
    class Converter {
        val gson = Gson()

        @TypeConverter
        fun fromListMember(value: List<String>): String {
            return value.toString()
        }

        @TypeConverter
        fun toListMember(value: String): List<String> {
            return gson.fromJson(value, object : TypeToken<List<String>>() {
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