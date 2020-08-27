package com.utsman.hiyahiyahiya.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Context.toast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.longToast(msg: String?) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
fun logi(msg: String?) = Log.i("HIYAHIYAHIYA", msg)

fun generateIdRoom(vararg ids: String) = ids.toList().sorted().toString()
    .replace("[", "")
    .replace("]", "")
    .replace(", ", "")
    .trim()
    .replace(" ", "")

fun generateIdStory(userId: String) = "story-of-$userId"
fun generateIdImageBB(userId: String) = "image-bb-of-$userId-${System.currentTimeMillis()}"

fun Activity.withPermissions(listPermission: List<String>, action: (grantedList: List<String>, deniedList: List<String>) -> Unit) {
    Dexter.withActivity(this)
        .withPermissions(listPermission)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    action.invoke(
                        report.grantedPermissionResponses.map { it.permissionName },
                        report.deniedPermissionResponses.map { it.permissionName })
                } else {
                    toast("Permission denied")
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }

        })
        .check()
}
