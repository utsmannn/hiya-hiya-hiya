package com.utsman.hiyahiyahiya.model

data class MessageBody(
    var fromMessage: String? = "",
    var toMessage: String? = "",
    var typeMessage: TypeMessage? = TypeMessage.DEVICE_REGISTER,
    var payload: Any? = null
)