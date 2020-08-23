package com.utsman.hiyahiyahiya

import android.app.Application
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.di.*
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HiyaHiyaHiyaApplication : Application() {

    companion object {
        lateinit var instance: HiyaHiyaHiyaApplication
        fun contextinism() = instance.applicationContext
    }

    override fun getApplicationContext(): Context {
        instance = this
        return super.getApplicationContext()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic(ConstantValue.topic)
        EmojiManager.install(GoogleEmojiProvider())
        startKoin {
            androidContext(this@HiyaHiyaHiyaApplication)
            modules(
                authModule,
                networkModule,
                dbModule,
                contactModule,
                adapterModule,
                roomModule,
                chatModule,
                photos
            )
        }
    }
}