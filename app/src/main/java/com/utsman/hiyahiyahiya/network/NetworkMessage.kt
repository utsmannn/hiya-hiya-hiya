package com.utsman.hiyahiyahiya.network

import android.content.ComponentCallbacks
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import com.utsman.hiyahiyahiya.model.MessageBody
import com.utsman.hiyahiyahiya.model.TypeMessage
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class NetworkMessage(componentCallbacks: ComponentCallbacks) {
    private val networkInstanceMessages: NetworkInstanceMessages by componentCallbacks.inject { parametersOf(ConstantValue.baseUrlFcm) }
    private val localUserDb: LocalUserDatabase by componentCallbacks.inject()

    fun send(activity: AppCompatActivity, messageBody: MessageBody, messageCallback: MessageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val targetUser = localUserDb.localUserDao().localUser(messageBody.toMessage ?: "")

            val rawBody = RawBody(
                to = if (messageBody.typeMessage == TypeMessage.DEVICE_REGISTER) {
                    "/topics/${ConstantValue.topic}"
                } else {
                    targetUser?.token
                },
                data = messageBody
            )

            val gson = Gson()
            val json = gson.toJson(rawBody)

            logi("raw body is -> $json")

            try {
                val response = networkInstanceMessages.sendMessage(rawBody)
                activity.runOnUiThread {
                    if (response.success == 1) {
                        messageCallback.onSuccess()
                    }
                    if (response.failure == 1) {
                        messageCallback.onFailed(null)
                    }
                }
            } catch (e: Throwable) {
                activity.runOnUiThread {
                    messageCallback.onFailed(e.localizedMessage)
                }
            }
        }
    }

    fun send(messageBody: MessageBody, messageCallback: MessageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val targetUser = localUserDb.localUserDao().localUser(messageBody.toMessage ?: "")

            val rawBody = RawBody(
                to = if (messageBody.typeMessage == TypeMessage.DEVICE_REGISTER) {
                    "/topics/${ConstantValue.topic}"
                } else {
                    targetUser?.token
                },
                data = messageBody
            )

            val gson = Gson()
            val json = gson.toJson(rawBody)
            logi("raw body is -> $json")

            try {
                val response = networkInstanceMessages.sendMessage(rawBody)
                if (response.success == 1) {
                    messageCallback.onSuccess()
                }
                if (response.failure == 1) {
                    messageCallback.onFailed(null)
                }
            } catch (e: Throwable) {
                messageCallback.onFailed(e.localizedMessage)
            }
        }
    }

    data class RawBody(
        val to: String?,
        val data: MessageBody
    )

    interface MessageCallback {
        fun onSuccess()
        fun onFailed(message: String?)
    }
}