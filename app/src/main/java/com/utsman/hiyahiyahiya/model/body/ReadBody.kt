package com.utsman.hiyahiyahiya.model.body

data class ReadBody(
    var otherId: String? = "",
    var itemid: String? = "",
    var hasRead: Boolean = false,
    var time: Long? = 0L
)