package com.utsman.hiyahiyahiya.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.UserPref
import com.utsman.hiyahiyahiya.di.network
import com.utsman.hiyahiyahiya.model.localUser
import com.utsman.hiyahiyahiya.model.messageBody
import com.utsman.hiyahiyahiya.network.TypeMessage
import com.utsman.hiyahiyahiya.network.NetworkMessage
import com.utsman.hiyahiyahiya.utils.*
import com.utsman.hiyahiyahiya.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private val networkMessage: NetworkMessage by network()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerNotification()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    logi("Instance Id failed")
                } else {
                    val token = task.result?.token
                    registerTokenToAnotherDevice(token)
                }
            }

        btn_contacts.click(MainScope()) {
            intentTo(ContactsActivity::class.java)
        }
    }

    private fun registerNotification() {

    }

    private fun registerTokenToAnotherDevice(tokenResult: String?) {
        authViewModel.requestUser()?.run {
            val profileAbout = "This about profile of $displayName"
            UserPref.saveUserId(uid)

            val localUser = localUser {
                id = uid
                name = displayName
                photoUri = photoUrl.toString()
                about = profileAbout
                token = tokenResult
            }

            val messageBody = messageBody {
                fromMessage = localUser.id
                typeMessage = TypeMessage.DEVICE_REGISTER
                payload = localUser
            }

            networkMessage.send(this@MainActivity, messageBody, object : NetworkMessage.MessageCallback {
                override fun onSuccess() {
                    toast("message sending")
                }

                override fun onFailed(message: String?) {
                    toast("failed -> $message")
                    tx_log.text = message
                }
            })
        }
    }
}