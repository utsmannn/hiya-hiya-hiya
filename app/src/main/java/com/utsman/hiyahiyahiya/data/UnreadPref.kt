package com.utsman.hiyahiyahiya.data

import android.content.Context
import com.utsman.hiyahiyahiya.HiyaHiyaHiyaApplication

object UnreadPref {
    private val pref = HiyaHiyaHiyaApplication.contextinism().getSharedPreferences("unread_count", Context.MODE_PRIVATE)

    fun saveCount(roomId: String, unReadCount: Int) {
        pref.edit().putInt("unread_$roomId", unReadCount).apply()
    }

    fun getUnreadCount(roomId: String) = pref.getInt("unread_$roomId", 0)
    fun clearUnreadCount(roomId: String) = pref.edit().remove("unread_$roomId").apply()
}