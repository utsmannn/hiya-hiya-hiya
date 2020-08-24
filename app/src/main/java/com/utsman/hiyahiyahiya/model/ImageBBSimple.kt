package com.utsman.hiyahiyahiya.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageBBSimple(
    val thumb: String?,
    val url: String?,
    @SerializedName("url_large")
    val urlLarge: String?
): Parcelable

fun ImageBB.toImageBBSimple(): ImageBBSimple {
    val thumb = data?.thumb?.url
    val url = data?.medium?.url
    val urlLarge = data?.image?.url
    return ImageBBSimple(thumb, url, urlLarge)
}