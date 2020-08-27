package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.database.*
import org.koin.dsl.module

val dbModule = module {
    single { LocalUserDatabase.getInstance(get()) }
    single { LocalRoomDatabase.getInstance(get()) }
    single { LocalChatDatabase.getInstance(get()) }
    single { LocalImageBBDatabase.getInstance(get()) }
    single { LocalStoryDatabase.getInstance(get()) }
}