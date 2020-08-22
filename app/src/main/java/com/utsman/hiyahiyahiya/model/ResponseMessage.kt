package com.utsman.hiyahiyahiya.model

data class ResponseMessage(
    val multicastId: Long,
    val success: Int,
    val failure: Int
)