package com.utsman.hiyahiyahiya.data

import android.content.Context
import com.utsman.hiyahiyahiya.HiyaHiyaHiyaApplication

object UserPref {
    private val pref = HiyaHiyaHiyaApplication.contextinism().getSharedPreferences("user_saved", Context.MODE_PRIVATE)

    fun saveUserId(userId: String) = pref.edit().putString("user_id", userId).apply()
    fun getUserId() = pref.getString("user_id", "") ?: ""

    fun saveUsername(username: String?) = pref.edit().putString("user_name", username).apply()
    fun getUsername() = pref.getString("user_name", "") ?: ""
}