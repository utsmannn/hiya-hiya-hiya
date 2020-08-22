package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.network.NetworkInstance
import org.koin.dsl.module

val networkModule = module {
    single { NetworkInstance.create() }
}