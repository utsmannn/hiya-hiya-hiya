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

    suspend fun calculateDivider(oldList: List<RowChatItem.ChatItem>): Flow<MutableList<RowChatItem.ChatItem>>? {
        val newList: MutableList<RowChatItem.ChatItem> = mutableListOf()
        var flowChat: Flow<MutableList<RowChatItem.ChatItem>>? = null
        CoroutineScope(Dispatchers.IO).launch {
            val iterator = oldList.iterator()
            var currentDay = 0
            for (item in iterator) {
                val day = sdfDay.format(item.time).toInt()

                logi("day --> $day")
                logi("message -> ${item.stringTime()}")
                if (day > currentDay) {
                    logi("divider here")
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
                    }

                    newList.add(chatDivider)
                }

                logi("add -> ${item.message}")
                newList.add(item)
            }
            flowChat = flow { emit(newList) }
        }
        return flowChat
    }

    fun calculateDividerList(oldList: List<RowChatItem.ChatItem>): MutableList<RowChatItem.ChatItem> {
        val newList: MutableList<RowChatItem.ChatItem> = mutableListOf()
        val iterator = oldList.iterator()
        var currentDay = 0
        for (item in iterator) {
            val day = sdfDay.format(item.time).toInt()

            logi("day --> $day")
            logi("message -> ${item.stringTime()}")
            if (day > currentDay) {
                logi("divider here")
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

            logi("add -> ${item.message}")
            newList.add(item)
        }
        return newList
    }
}