package com.utsman.hiyahiyahiya.data

import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowAttachChat
import com.utsman.hiyahiyahiya.model.utils.rowAttachment

object ConstantValue {
    const val topicRegister = "hiya_hiya_hiya"
    const val topicStory = "hiya_hiya_hiya_story"
    const val serverKey =
        "AAAA383KWAE:APA91bG5SqtplnIPkrssJq16fwP5n_8om0IoODbCWCWrLKLlMLBjF9hjTJ1uwU41zz3Lz3laSR9iEWCKLjO_MWOi2u3HVcK8Zuo3qFo1rTPF0gogEljZznL0AlAT7QvSCYKpaQ0jP9GH"
    const val imageBBKey =
        "bd08a04c5c3f4fad78876566c7b7e895"

    const val baseUrlFcm = "https://fcm.googleapis.com"
    const val baseUrlImageBB = "https://api.imgbb.com"

    val itemAttachments = mutableListOf<RowAttachChat>().apply {
        add(rowAttachment {
            this.id = "camera"
            this.bgResource = R.drawable.bg_camera
            this.iconResource = R.drawable.ic_round_photo_camera_24
            this.title = "Camera"
        })

        add(rowAttachment {
            this.id = "gallery"
            this.bgResource = R.drawable.bg_gallery
            this.iconResource = R.drawable.ic_baseline_photo_24
            this.title = "Gallery"
        })

        add(rowAttachment {
            this.id = "location"
            this.bgResource = R.drawable.bg_maps
            this.iconResource = R.drawable.ic_baseline_location_on_24
            this.title = "Location"
        })
    }
}