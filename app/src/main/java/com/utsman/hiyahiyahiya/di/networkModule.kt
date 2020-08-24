package com.utsman.hiyahiyahiya.di

import com.utsman.hiyahiyahiya.network.NetworkInstanceMessages
import com.utsman.hiyahiyahiya.network.NetworkInstanceImageBB
import org.koin.dsl.module

val networkModule = module {
    single { NetworkInstanceMessages.create() }
    single { NetworkInstanceImageBB.create() }
}