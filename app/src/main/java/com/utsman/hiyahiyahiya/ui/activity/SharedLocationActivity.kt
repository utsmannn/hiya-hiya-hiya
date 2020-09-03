package com.utsman.hiyahiyahiya.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.DatePref
import com.utsman.hiyahiyahiya.data.shared_location.SharedLocationManager
import com.utsman.hiyahiyahiya.services.LocationWorker
import com.utsman.hiyahiyahiya.ui.adapter.SharedLocationAdapter
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.android.synthetic.main.activity_shared_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SharedLocationActivity : AppCompatActivity() {

    private val locationManager by lazy { SharedLocationManager(this) }
    private val sharedLocationAdapter: SharedLocationAdapter by inject()

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_location)
        (map_view as SupportMapFragment).getMapAsync { maps ->
            maps.isMyLocationEnabled = true
            GlobalScope.launch {
                val myLocation = locationManager.getLocation()
                myLocation?.let { loc ->
                    GlobalScope.launch(Dispatchers.Main) {
                        maps.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude, loc.longitude), 15f))
                    }
                }
            }
        }

        rv_shared_location.run {
            layoutManager = LinearLayoutManager(this@SharedLocationActivity)
            adapter = sharedLocationAdapter
        }

        val duration: Long = 10000
        val inputDataDuration = workDataOf(
            "duration" to duration,
            "id" to "uhuy"
        )

        val requestWorker = OneTimeWorkRequestBuilder<LocationWorker>()
            .setInputData(inputDataDuration)
            .build()

        WorkManager.getInstance(this).apply {

            /*btn_work.click {
                DatePref.saveTime("uhuy")
                enqueue(requestWorker)
            }*/

            sharedLocationAdapter.onItemClick {
                if (it.id == 1) {
                    DatePref.saveTime("uhuy")
                    enqueue(requestWorker)
                }
            }

            getWorkInfoByIdLiveData(requestWorker.id)
                .observe(this@SharedLocationActivity, Observer { workInfo ->
                    logi("work state is -> ${workInfo.state}")

                    val result = when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> workInfo.outputData.getString("location")
                        WorkInfo.State.RUNNING -> workInfo.progress.getString("location")
                        else -> "failed"
                    }
                })
        }
    }
}