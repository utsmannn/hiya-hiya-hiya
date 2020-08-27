package com.utsman.hiyahiyahiya.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.utsman.hiyahiyahiya.ui.viewmodel.AuthViewModel
import com.utsman.hiyahiyahiya.R
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()
    private val viewModel2: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        registerNotification()

        val user = viewModel.requestUser()
        updateUi(user, null)

        btn_login.setOnClickListener {
            viewModel.login(this) { user, s ->
                updateUi(user, s)
            }
        }
    }

    private fun registerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameDefault = "Message notification"
            val descriptionTextDefault = "Notify when incoming message"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("hiya-hiya", nameDefault, importance).apply {
                description = descriptionTextDefault
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(user: FirebaseUser?, errorLog: String?) {
        progress_circular.visibility = View.GONE
        if (user != null) {
            tx_log.visibility = View.GONE
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            tx_log.visibility = View.VISIBLE
            tx_log.text = errorLog
        }
    }
}