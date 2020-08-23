package com.utsman.hiyahiyahiya.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.utsman.hiyahiyahiya.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

fun EditText.debounce(delay: Long, subscribe: (String) -> Unit) {
    var resultFor = ""

    addTextChangedListener {
        doOnTextChanged { text, _, _, _ ->
            if ((text?.length ?: 0) >= 1) {
                val resultText = text.toString().trim()
                if (resultText == resultFor)
                    return@doOnTextChanged

                resultFor = resultText

                CoroutineScope(Dispatchers.Main).launch {
                    kotlinx.coroutines.delay(delay)
                    if (resultText != resultFor)
                        return@launch

                    subscribe.invoke(resultFor)
                }
            }
        }
    }
}

fun KeyboardVisibilityListener.setKeyboardVisibilityListener(parent: View) {
    parent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        private var mPreviousHeight = 0
        override fun onGlobalLayout() {
            val newHeight = parent.height
            if (mPreviousHeight != 0) {
                when {
                    mPreviousHeight > newHeight -> {
                        onKeyboardVisibilityChanged(true)
                    }
                    mPreviousHeight < newHeight -> {
                        onKeyboardVisibilityChanged(false)
                    }
                }
            }
            mPreviousHeight = newHeight
        }
    })
}