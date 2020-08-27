package com.utsman.hiyahiyahiya.model.body

import com.google.gson.annotations.SerializedName
import com.utsman.hiyahiyahiya.model.features.ImageBBSimple

data class StoryBody(
    @SerializedName("user_id")
    var userId: String = "",
    var time: Long = 0L,
    @SerializedName("image_bb")
    var imageBB: ImageBBSimple = ImageBBSimple()
)