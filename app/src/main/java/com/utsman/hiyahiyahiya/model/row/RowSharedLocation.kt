package com.utsman.hiyahiyahiya.model.row

import androidx.annotation.DrawableRes
import com.utsman.hiyahiyahiya.R

data class RowSharedLocation(
    var id: Int = 0,
    @DrawableRes var icon: Int = R.drawable.bg_maps,
    var title: String = ""
)