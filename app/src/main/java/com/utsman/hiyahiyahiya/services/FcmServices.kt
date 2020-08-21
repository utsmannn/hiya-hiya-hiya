package com.utsman.hiyahiyahiya.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.utsman.hiyahiyahiya.utils.logi

class FcmServices : FirebaseMessagingService() {

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        logi("message arrived -> ${remote.data}")
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}
