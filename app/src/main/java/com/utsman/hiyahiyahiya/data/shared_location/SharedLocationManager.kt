package com.utsman.hiyahiyahiya.data.shared_location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SharedLocationManager(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Location? = suspendCoroutine { task ->
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener {
                task.resume(it)
            }
    }
}