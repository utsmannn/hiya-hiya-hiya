package com.utsman.hiyahiyahiya.model.row

enum class RowType {
    ITEM,
    EMPTY
}

sealed class RowContact(val rowType: RowType) {
    data class Contact(
        var id: String = "",
        var name: String? = "",
        var photoUrl: String? = "",
        var about: String? = ""
    ) : RowContact(RowType.ITEM)

    data class Empty(
        var text: String = "Empty"
    ) : RowContact(RowType.EMPTY)
}