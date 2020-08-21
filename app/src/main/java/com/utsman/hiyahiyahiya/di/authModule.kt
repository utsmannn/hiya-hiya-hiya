package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.viewmodel.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val auth = module {
    viewModel { AuthViewModel(get()) }
}