package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.data.repository.*
import com.utsman.hiyahiyahiya.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val auth = module {
    viewModel { AuthViewModel(get()) }
}

val contact = module {
    single { ContactRepository(get()) }
    viewModel { ContactViewModel(get()) }
}

val room = module {
    single { RoomRepository(get()) }
    viewModel { RoomViewModel(get()) }
}

val chat = module {
    single { ChatRepository(get()) }
    viewModel { ChatViewModel(get()) }
}

val photos = module {
    single { PhotosRepository(get()) }
    viewModel { PhotosViewModel(get()) }
}

val story = module {
    single { StoryRepository(get(), get()) }
    viewModel { StoryViewModel(get(), get()) }
}