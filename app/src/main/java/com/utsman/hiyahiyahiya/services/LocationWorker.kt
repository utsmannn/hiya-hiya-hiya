package com.utsman.hiyahiyahiya.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.utsman.hiyahiyahiya.data.shared_location.SharedLocationManager
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class LocationWorker(private val context: Context, param: WorkerParameters) : CoroutineWorker(context, param) {

    override suspend fun doWork(): Result = coroutineScope {
        val locationManager = SharedLocationManager(context)

        val duration = inputData.getLong("duration", 0)
        logi("duration is -> $duration")
        var outputData: Data = workDataOf("location" to "empty")

        delay(1000)
        for (i in 1..(duration*2)) {
            delay(500)

            logi("getting location .... position duration -> $i")
            val location = locationManager.getLocation()
            val locationString = "${location?.latitude}, ${location?.longitude}"
            logi("location is -> $locationString")
            outputData = workDataOf("location" to locationString)
            setProgress(outputData)

            if (i >= (duration*2)) return@coroutineScope Result.success(outputData)
        }

        Result.success(outputData)
    }
}