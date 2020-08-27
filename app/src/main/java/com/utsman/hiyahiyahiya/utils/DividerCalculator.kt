package com.utsman.hiyahiyahiya.utils

import android.annotation.SuppressLint
import com.utsman.hiyahiyahiya.model.row.RowChatItem
import com.utsman.hiyahiyahiya.model.row.RowChatType
import com.utsman.hiyahiyahiya.model.utils.chatItem
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

    fun calculateDividerList(oldList: List<RowChatItem.ChatItem>): MutableList<RowChatItem.ChatItem> {
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
}