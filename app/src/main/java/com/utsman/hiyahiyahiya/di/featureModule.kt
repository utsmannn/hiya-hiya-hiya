package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.data.repository.ChatRepository
import com.utsman.hiyahiyahiya.data.repository.ContactRepository
import com.utsman.hiyahiyahiya.data.repository.PhotosRepository
import com.utsman.hiyahiyahiya.data.repository.RoomRepository
import com.utsman.hiyahiyahiya.ui.viewmodel.ChatViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.ContactViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.PhotosViewModel
import com.utsman.hiyahiyahiya.ui.viewmodel.RoomViewModel
import com.utsman.hiyahiyahiya.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel { AuthViewModel(get()) }
}

val contactModule = module {
    single { ContactRepository(get()) }
    viewModel { ContactViewModel(get()) }
}

val roomModule = module {
    single { RoomRepository(get()) }
    viewModel { RoomViewModel(get()) }
}

val chatModule = module {
    single { ChatRepository(get()) }
    viewModel { ChatViewModel(get()) }
}

val photos = module {
    single { PhotosRepository(get()) }
    viewModel { PhotosViewModel(get()) }
}