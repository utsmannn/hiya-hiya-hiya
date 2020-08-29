package com.utsman.hiyahiyahiya.data.shared_location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

class SharedLocationManager(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getUpdateLocation(result: (Location?) -> Unit) {
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                result.invoke(location)
            }
    }
}