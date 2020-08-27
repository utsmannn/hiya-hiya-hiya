package com.utsman.hiyahiyahiya.model.body

import com.utsman.hiyahiyahiya.model.types.TypeMessage

data class MessageBody(
    var fromMessage: String? = "",
    var toMessage: String? = "",
    var typeMessage: TypeMessage? = TypeMessage.DEVICE_REGISTER,
    var payload: Any? = null
)