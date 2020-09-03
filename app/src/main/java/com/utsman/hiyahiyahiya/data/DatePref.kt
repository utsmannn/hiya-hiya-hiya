package com.utsman.hiyahiyahiya.data

import android.content.Context
import com.utsman.hiyahiyahiya.HiyaHiyaHiyaApplication
import com.utsman.hiyahiyahiya.utils.logi

object DatePref {
    private val pref = HiyaHiyaHiyaApplication.contextinism().getSharedPreferences("date", Context.MODE_PRIVATE)

    fun saveTime(id: String?) {
        val time = System.currentTimeMillis()
        pref.edit().putLong("millis_$id", time).apply()
    }

    fun getTime(id: String?) = pref.getLong("millis_$id", 0L)

    fun isBefore(id: String?, distanceTime: Long): Boolean {
        val now = System.currentTimeMillis()
        val savedPref = getTime(id)
        logi("now -> $now saved -> $savedPref result -> ${now - savedPref}")
        return when (distanceTime) {
            0L -> false
            else -> now - savedPref <= distanceTime
        }
    }
}