package com.utsman.hiyahiyahiya.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.utsman.hiyahiyahiya.network.NetworkMessage

fun Activity.network(): Lazy<NetworkMessage> = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkMessage(this) }
fun Fragment.network(): Lazy<NetworkMessage> = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { NetworkMessage(this) }