package com.utsman.hiyahiyahiya.model.features

data class ResponseMessage(
    val multicastId: Long,
    val success: Int,
    val failure: Int
)