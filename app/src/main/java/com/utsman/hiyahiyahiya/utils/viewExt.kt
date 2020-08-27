package com.utsman.hiyahiyahiya.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
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

private fun Context.getStatusBarHeight(): Int {
    var statusBarHeight = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}

fun View.goneAnimation() {
    animate()
        .alpha(0f)
        .setDuration(400)
        .withStartAction {
            visibility = View.GONE
        }
        .start()
}

fun View.visibleAnimation(alpha: Float) {
    animate()
        .alpha(alpha)
        .setDuration(400)
        .withStartAction {
            visibility = View.VISIBLE
        }
        .start()
}

fun Activity.makeStatusBarTransparent(pushPadding: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.setBackgroundColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 0
                if (pushPadding) {
                    val parentView = findViewById<ViewGroup>(android.R.id.content)
                    parentView.setPadding(0, context.getStatusBarHeight(), 0, 0)
                }
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Int.dp(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

fun Activity.startPermission(accept: () -> Unit) {
    val listPermission = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    withPermissions(listPermission) { _, deniedList ->
        if (deniedList.isEmpty()) {
            accept.invoke()
        }
    }
}