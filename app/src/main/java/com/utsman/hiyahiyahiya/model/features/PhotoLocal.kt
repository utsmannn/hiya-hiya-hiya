package com.utsman.hiyahiyahiya.model.features

data class PhotoLocal(
    var date: String = "",
    var uri: String = "",
    var dividerString: String = ""
): RowPhoto(if (dividerString.isNotEmpty()) PhotoType.ITEM else PhotoType.DIVIDER)

enum class PhotoType {
    ITEM, DIVIDER
}

sealed class RowPhoto(var photoType: PhotoType)