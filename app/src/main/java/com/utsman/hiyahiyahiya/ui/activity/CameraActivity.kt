package com.utsman.hiyahiyahiya.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.row.RowRoom
import com.utsman.hiyahiyahiya.ui.fragment.CameraFragment


class CameraActivity : AppCompatActivity() {

    private val room by lazy { intent.getParcelableExtra<RowRoom.RoomItem>("room") }
    private val toMember by lazy { intent.getStringExtra("to") }
    private val intentType by lazy { intent.getStringExtra("intent_type") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val cameraFragment = CameraFragment.instance(room, toMember, intentType)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_camera, cameraFragment)
            .commit()
    }
}