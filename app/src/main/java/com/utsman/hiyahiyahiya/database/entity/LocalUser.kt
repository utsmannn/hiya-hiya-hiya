package com.utsman.hiyahiyahiya.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "local_user")
@Parcelize
data class LocalUser(
    @PrimaryKey
    var id: String = "utsman-gantenk",
    var name: String? = "",
    @SerializedName("photo_url")
    var photoUri: String? = "",
    var about: String? = "",
    var token: String? = ""
) : Parcelable