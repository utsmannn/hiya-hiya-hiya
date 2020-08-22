package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.ui.adapter.ChatAdapter
import com.utsman.hiyahiyahiya.ui.adapter.ContactAdapter
import com.utsman.hiyahiyahiya.ui.adapter.RoomAdapter
import org.koin.dsl.module

val adapterModule = module {
    single { ContactAdapter() }
    factory { ChatAdapter() }
    factory { RoomAdapter() }
}