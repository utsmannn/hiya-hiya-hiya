package com.utsman.hiyahiyahiya

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import com.utsman.hiyahiyahiya.di.auth
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HiyaHiyaHiyaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("hiya_hiya")
        startKoin {
            androidContext(this@HiyaHiyaHiyaApplication)
            modules(auth)
        }
    }
}