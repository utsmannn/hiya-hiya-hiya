package com.utsman.hiyahiyahiya.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.longToast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
fun logi(msg: String?) = Log.i("HIYAHIYAHIYA", msg)

fun generateId() = System.currentTimeMillis().toString().takeLast(5).toInt()
fun generateIdRoom(vararg ids: String) = ids.toList().sorted().toString()
    .replace("[", "")
    .replace("]", "")
    .replace(", ", "")
    .trim()
    .replace(" ", "")