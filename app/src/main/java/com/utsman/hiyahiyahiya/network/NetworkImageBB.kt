package com.utsman.hiyahiyahiya.network

import android.app.Activity
import android.content.ComponentCallbacks
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.model.ImageBBSimple
import com.utsman.hiyahiyahiya.model.toImageBBSimple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NetworkImageBB(componentCallbacks: ComponentCallbacks) {
    private val networkInstanceImageBB: NetworkInstanceImageBB by componentCallbacks.inject()

    fun uploadImage(activity: Activity, base64: String, imageBBCallback: ImageBBCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkInstanceImageBB.postToImageBB(ConstantValue.imageBBKey, base64)
                if (response.success == true) {
                    activity.runOnUiThread {
                        imageBBCallback.onSuccess(response.toImageBBSimple())
                    }
                } else {
                    activity.runOnUiThread {
                        imageBBCallback.onFailed("Failed")
                    }
                }
            } catch (e: Throwable) {
                activity.runOnUiThread {
                    imageBBCallback.onFailed(e.localizedMessage)
                }
            }
        }
    }

    interface ImageBBCallback {
        fun onSuccess(imageBB: ImageBBSimple)
        fun onFailed(message: String?)
    }
}