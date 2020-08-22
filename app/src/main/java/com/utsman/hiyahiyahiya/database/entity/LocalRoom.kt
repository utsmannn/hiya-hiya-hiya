package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "local_room")
data class LocalRoom(
    @PrimaryKey
    var id: String = "ariel-tatum",
    var chatsId: List<String> = emptyList(),
    var membersId: List<String> = emptyList(),
    var lastDate: Long = 0L
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
    }
}