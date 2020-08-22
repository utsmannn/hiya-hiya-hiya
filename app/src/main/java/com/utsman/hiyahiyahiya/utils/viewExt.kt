package com.utsman.hiyahiyahiya.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
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

fun ImageView.load(url: String?, isCircle: Boolean = false) {
    val picasso = Picasso.get().load(url)
    if (isCircle) picasso.transform(CropCircleTransformation())
    picasso.into(this)
}

fun Activity.intentTo(
    c: Class<*>,
    options: ActivityOptions? = null,
    extIntent: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, c)
    extIntent?.invoke(intent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (options != null) {
            startActivity(intent, options.toBundle())
        } else {
            val optionDefault = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, optionDefault.toBundle())
        }
    } else {
        startActivity(intent)
    }
}

fun Fragment.intentTo(
    c: Class<*>,
    options: ActivityOptions? = null,
    extIntent: (Intent.() -> Unit)? = null
) {
    val intent = Intent(context, c)
    extIntent?.invoke(intent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (options != null) {
            startActivity(intent, options.toBundle())
        } else {
            val optionDefault = ActivityOptions.makeSceneTransitionAnimation(activity)
            startActivity(intent, optionDefault.toBundle())
        }
    } else {
        startActivity(intent)
    }
}