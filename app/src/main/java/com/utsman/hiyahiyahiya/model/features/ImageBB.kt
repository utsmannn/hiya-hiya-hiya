package com.utsman.hiyahiyahiya.model.features

data class ImageBB(
    val `data`: Data?,
    val status: Int?,
    val success: Boolean?
) {
    data class Data(
        val deleteUrl: String?,
        val displayUrl: String?,
        val expiration: String?,
        val id: String?,
        val image: Image?,
        val medium: Medium?,
        val size: Int?,
        val thumb: Thumb?,
        val time: String?,
        val title: String?,
        val url: String?,
        val urlViewer: String?
    ) {
        data class Image(
            val extension: String?,
            val filename: String?,
            val mime: String?,
            val name: String?,
            val url: String?
        )

        data class Medium(
            val extension: String?,
            val filename: String?,
            val mime: String?,
            val name: String?,
            val url: String?
        )

        data class Thumb(
            val extension: String?,
            val filename: String?,
            val mime: String?,
            val name: String?,
            val url: String?
        )
    }
}