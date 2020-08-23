package com.utsman.hiyahiyahiya.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.utsman.hiyahiyahiya.model.PhotoLocal
import com.utsman.hiyahiyahiya.model.photo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotosRepository(private val context: Context) {

    @SuppressLint("Recycle")
    suspend fun allImagesPath(): Flow<PhotoLocal> {
        var columnIndexData: Int
        var columnIndexDate: Int
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        return flow {
            if (cursor != null) {
                columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                columnIndexDate = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val absolutePathOfImage = cursor.getString(columnIndexData)
                    val dateOfImage = cursor.getString(columnIndexDate)
                    val photoLocal = photo {
                        this.date = dateOfImage
                        this.uri = absolutePathOfImage
                    }
                    emit(photoLocal)
                }
                cursor.close()
            }
        }
    }
}