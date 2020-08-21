package com.utsman.hiyahiyahiya.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
fun logi(msg: String) = Log.i("HIYAHIYAHIYA", msg)