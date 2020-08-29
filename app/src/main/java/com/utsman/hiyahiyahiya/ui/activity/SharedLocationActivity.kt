package com.utsman.hiyahiyahiya.ui.activity

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.shared_location.SharedLocationManager
import com.utsman.hiyahiyahiya.services.LocationWorker
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.android.synthetic.main.activity_shared_location.*
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class SharedLocationActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_location)

        tx_test.text = "updating"

        val duration: Long = 30
        val inputDataDuration = workDataOf("duration" to duration)
        val requestWorker = OneTimeWorkRequestBuilder<LocationWorker>()
            .setInputData(inputDataDuration)
            .build()

        WorkManager.getInstance(this).apply {
            enqueue(requestWorker)
            getWorkInfoByIdLiveData(requestWorker.id)
                .observe(this@SharedLocationActivity, Observer { workInfo ->
                    logi("work state is -> ${workInfo.state}")

                    val result = when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> workInfo.outputData.getString("location")
                        WorkInfo.State.RUNNING -> workInfo.progress.getString("location")
                        else -> "failed"
                    }

                    tx_test.text = result
                })
        }
    }
}