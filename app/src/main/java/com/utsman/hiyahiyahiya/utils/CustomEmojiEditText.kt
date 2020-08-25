package com.utsman.hiyahiyahiya.utils

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.vanniktech.emoji.EmojiEditText

class CustomEmojiEditText(context: Context, attributeSet: AttributeSet) : EmojiEditText(context, attributeSet) {

    private val sizeChanged: MutableLiveData<Int> = MutableLiveData()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeChanged.postValue(h)
        logi("height =------> $h")
    }

    fun observerHeightChanges(lifecycleOwner: LifecycleOwner, newHeight: (Int) -> Unit) {
        sizeChanged.observe(lifecycleOwner, Observer {
            newHeight.invoke(it)
        })
    }
}