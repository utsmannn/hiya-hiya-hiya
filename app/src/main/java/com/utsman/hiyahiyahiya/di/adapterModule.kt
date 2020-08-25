package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.ui.adapter.*
import org.koin.dsl.module

val adapterModule = module {
    single { ContactAdapter() }
    factory { ChatAdapter() }
    factory { RoomAdapter() }
    factory { PhotoAdapter() }
    factory { PhotosPagedAdapter() }
}