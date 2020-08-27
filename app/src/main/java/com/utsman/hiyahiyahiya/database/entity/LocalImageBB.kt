package com.utsman.hiyahiyahiya.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.utsman.hiyahiyahiya.model.features.ImageBBSimple

@Entity(tableName = "local_image_bb")
data class LocalImageBB(
    @PrimaryKey
    var id: String = "",
    var time: Long = 0L,
    @SerializedName("image_bb_simple")
    var imageBBSimple: ImageBBSimple = ImageBBSimple()
) {

    class Converter {
        val gson = Gson()

        @TypeConverter
        fun fromImageBbSimple(value: ImageBBSimple): String {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toImageBbSimple(value: String): ImageBBSimple {
            return gson.fromJson(value, object : TypeToken<ImageBBSimple>() {
            }.type)
        }
    }
}