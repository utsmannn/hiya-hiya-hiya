package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.ui.adapter.*
import com.utsman.hiyahiyahiya.ui.adapter.chat_viewholder.AttachmentAdapter
import org.koin.dsl.module

val adapterModule = module {
    single { ContactAdapter() }
    factory { ChatAdapter() }
    factory { RoomAdapter() }
    factory { PhotosPagedAdapter() }
    factory { StoryAdapter() }
    factory { AttachmentAdapter() }
    single { SharedLocationAdapter() }
}