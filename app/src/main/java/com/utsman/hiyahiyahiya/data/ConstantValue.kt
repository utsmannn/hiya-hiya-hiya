package com.utsman.hiyahiyahiya.data

import com.utsman.hiyahiyahiya.BuildConfig
import com.utsman.hiyahiyahiya.HiyaHiyaHiyaApplication
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowAttachChat
import com.utsman.hiyahiyahiya.model.utils.rowAttachment
import com.utsman.hiyahiyahiya.model.utils.rowSharedLocation

object ConstantValue {
    const val topicRegister = "hiya_hiya_hiya"
    const val topicStory = "hiya_hiya_hiya_story"
    const val serverKey = BuildConfig.FCM_API_KEY
    const val imageBBKey = BuildConfig.IMGBB_API_KEY

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

    val itemRowSharedLocation = listOf(
        rowSharedLocation {
            this.id = 1
            this.title = "Shared live location"
            this.icon = R.drawable.ic_round_my_location_24
        },
        rowSharedLocation {
            this.id = 2
            this.title = "Send your current location"
            this.icon = R.drawable.ic_baseline_adjust_24
        }
    )
}