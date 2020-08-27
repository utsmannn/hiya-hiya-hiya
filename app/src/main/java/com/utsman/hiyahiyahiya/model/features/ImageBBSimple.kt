package com.utsman.hiyahiyahiya.model.features

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageBBSimple(
    val thumb: String? = null,
    val url: String? = null,
    @SerializedName("url_large")
    val urlLarge: String? = null
): Parcelable