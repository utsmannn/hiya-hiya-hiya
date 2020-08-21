package com.utsman.hiyahiyahiya.utils

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private fun View.clicks(): Flow<Unit> = callbackFlow {
    this@clicks.setOnClickListener {
        this.offer(Unit)
    }
    awaitClose { this@clicks.setOnClickListener(null) }
}

fun View.click(scope: CoroutineScope, action: View.() -> Unit) {
    clicks().onEach { action.invoke(this) }.launchIn(scope)
}

fun View.click(view: (View) -> Unit) {
    setOnClickListener {
        view.invoke(it)
    }
}