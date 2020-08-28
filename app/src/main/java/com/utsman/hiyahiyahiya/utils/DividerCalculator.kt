package com.utsman.hiyahiyahiya.utils

import android.annotation.SuppressLint
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.model.features.PhotoType
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowChatType
import com.utsman.hiyahiyahiya.model.utils.chatItem
import com.utsman.hiyahiyahiya.model.utils.photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
object DividerCalculator {

    private val sdfDay = SimpleDateFormat("DD")
    private val sdfDate = SimpleDateFormat("EEE dd/MMM/yyyy")
    private val nowDay by lazy {
        sdfDay.format(System.currentTimeMillis()).toInt()
    }

    fun calculateDividerChat(oldList: List<RowChatItem.ChatItem>): MutableList<RowChatItem.ChatItem> {
        val newList: MutableList<RowChatItem.ChatItem> = mutableListOf()
        val iterator = oldList.iterator()
        var currentDay = 0
        for (item in iterator) {
            val day = sdfDay.format(item.time).toInt()
            if (day > currentDay) {
                val newIdDivider = "divider-$day"

                currentDay = day
                val messageDivider = when (currentDay) {
                    nowDay -> {
                        "Hari ini"
                    }
                    nowDay - 1 -> {
                        "Kemarin"
                    }
                    else -> {
                        sdfDate.format(item.time)
                    }
                }

                val chatDivider = chatItem {
                    id = newIdDivider
                    divider = true
                    message = messageDivider
                    time = item.time
                }.apply {
                    rowChatType = RowChatType.DIVIDER
                }

                newList.add(chatDivider)
            }

            newList.add(item)
        }
        return newList
    }

    fun calculateDividerGallery(oldList: List<PhotoLocal>): MutableList<PhotoLocal> {
        val newList: MutableList<PhotoLocal> = mutableListOf()
        val iterator = oldList.iterator()
        var currentDay = 0
        for (item in iterator) {
            logi(item.toString())
            val newTime = item.date.toLong()*1000
            val day = sdfDay.format(newTime).toInt()
            if (day > currentDay) {
                val newIdDivider = "divider-$day"
                currentDay = day
                val messageDivider = when (currentDay) {
                    nowDay -> {
                        "Hari ini"
                    }
                    nowDay - 1 -> {
                        "Kemarin"
                    }
                    else -> {
                        sdfDate.format(newTime)
                    }
                }

                logi("divider here --> $messageDivider")
                val photoDivider = photo {
                    dividerString = messageDivider
                }.apply {
                    photoType = PhotoType.DIVIDER
                }

                newList.add(photoDivider)
            }

            newList.add(item.apply { photoType = PhotoType.ITEM })
        }
        return newList
    }
}