package com.utsman.hiyahiyahiya.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun ByteArray.saveImage(fixRotation: Boolean = true, validation: Boolean = false, result: (File) -> Unit) {
    try {
        val folder = File(Environment.getExternalStorageDirectory(), "WA_h3")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val fileName = "/WA_h3/HIYA_HIYA_HIYA_" + System.currentTimeMillis() + ".jpg"
        val file = File(Environment.getExternalStorageDirectory(), fileName)
        if (file.exists()) {
            file.delete()
        }

        try {
            val fos = FileOutputStream(file)
            var realImage = BitmapFactory.decodeByteArray(this, 0, this.size)
            if (fixRotation) {
                val exif = ExifInterface(file.toString())
                when {
                    exif.getAttribute(ExifInterface.TAG_ORIENTATION) == "6" -> {
                        realImage = rotate(realImage, 90f)
                    }
                    exif.getAttribute(ExifInterface.TAG_ORIENTATION) == "8" -> {
                        realImage = rotate(realImage, 270f)
                    }
                    exif.getAttribute(ExifInterface.TAG_ORIENTATION) == "3" -> {
                        realImage = rotate(realImage, 0f)
                    }
                    exif.getAttribute(ExifInterface.TAG_ORIENTATION) == "0" -> {
                        realImage = if (validation) {
                            rotate(realImage, 270f)
                        } else {
                            rotate(realImage, 90f)
                        }
                    }
                }
            }

            realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        result.invoke(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun rotate(bitmap: Bitmap, degree: Float): Bitmap? {
    val w = bitmap.width
    val h = bitmap.height
    val mtx = Matrix()
    mtx.setRotate(degree)
    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
}

fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.DEFAULT)
}

fun String.toBytes64(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}