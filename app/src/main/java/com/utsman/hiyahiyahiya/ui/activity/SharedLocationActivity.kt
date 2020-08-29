package com.utsman.hiyahiyahiya.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.shared_location.SharedLocationManager
import kotlinx.android.synthetic.main.activity_shared_location.*

class SharedLocationActivity : AppCompatActivity() {

    private val locationManager by lazy { SharedLocationManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_location)

        tx_test.text = "updating"
        locationManager.getUpdateLocation {
            tx_test.text = it.toString()
        }
    }
}