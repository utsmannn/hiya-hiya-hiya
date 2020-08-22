package com.utsman.hiyahiyahiya.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class MessageAttachment {
    @Parcelize
    data class ImageAttachment(val url: String): Parcelable, MessageAttachment()
    @Parcelize
    data class UrlAttachment(val url: String): Parcelable, MessageAttachment()
}