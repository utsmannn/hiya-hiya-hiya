package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "local_story")
data class LocalStory(
    @PrimaryKey
    var id: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    var time: Long = 0L,
    @SerializedName("local_imagebb_ids")
    var localImageBBIds: List<String> = emptyList()
) {
    class Converter {
        val gson = Gson()

        @TypeConverter
        fun fromListLocalImageBBIds(value: List<String>): String {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toListLocalImageBBIds(value: String): List<String> {
            return gson.fromJson(value, object : TypeToken<List<String>>() {
            }.type)
        }
    }
}