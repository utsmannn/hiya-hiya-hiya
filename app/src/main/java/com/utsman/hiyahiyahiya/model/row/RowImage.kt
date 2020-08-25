package com.utsman.hiyahiyahiya.model.row

enum class RowImageType {
    PHOTO_1, PHOTO_2
}

sealed class RowImage(var rowImageType: RowImageType) {
    data class Item1(var uri: String) : RowImage(RowImageType.PHOTO_1)
    data class Item2(var uri: String) : RowImage(RowImageType.PHOTO_2)
}