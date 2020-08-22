package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.database.LocalChatDatabase
import com.utsman.hiyahiyahiya.database.LocalRoomDatabase
import com.utsman.hiyahiyahiya.database.LocalUserDatabase
import org.koin.dsl.module

val dbModule = module {
    single { LocalUserDatabase.getInstance(get()) }
    single { LocalRoomDatabase.getInstance(get()) }
    single { LocalChatDatabase.getInstance(get()) }
}