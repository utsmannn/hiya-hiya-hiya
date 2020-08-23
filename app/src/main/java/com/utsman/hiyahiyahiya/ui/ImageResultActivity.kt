package com.utsman.hiyahiyahiya.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import kotlinx.android.synthetic.main.activity_image_result.*
import java.io.File

class ImageResultActivity : AppCompatActivity() {

    private val imagePath by lazy {
        intent.getStringExtra("image_path")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_result)

        val file = File(imagePath)
        Picasso.get().load(file).into(img_result)
    }
}