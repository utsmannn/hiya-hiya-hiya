package com.utsman.hiyahiyahiya.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.utsman.hiyahiyahiya.database.LocalChatDatabase
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import com.utsman.hiyahiyahiya.network.TypeMessage
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmServices : FirebaseMessagingService() {
    private val gson = Gson()
    private val localUserDb: LocalUserDatabase by inject()
    private val localChatDb: LocalChatDatabase by inject()

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        logi("message from arrived -> ${remote.from}")
        logi("message arrived -> ${remote.data}")

        val type = remote.data["type_message"]
        val payloadString = remote.data["payload"]
        when (type) {
            TypeMessage.DEVICE_REGISTER.name -> {
                val payloadUser = gson.fromJson(payloadString, LocalUser::class.java)
                GlobalScope.launch {
                    localUserDb.localUserDao().insert(payloadUser)
                }
            }
            TypeMessage.MESSAGE.name -> {
                val payloadChat = gson.fromJson(payloadString, LocalChat::class.java)
                GlobalScope.launch {
                    localChatDb.localChatDao().insert(payloadChat)
                }
            }
        }


        logi("message type is -->> $type ")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}
