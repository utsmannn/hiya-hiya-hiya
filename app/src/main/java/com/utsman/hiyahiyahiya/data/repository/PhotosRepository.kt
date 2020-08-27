package com.utsman.hiyahiyahiya.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.model.utils.photo
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.ConcurrentModificationException

class PhotosRepository(private val context: Context) {

    @SuppressLint("Recycle")
    suspend fun allImagesFlowReorder(): Flow<MutableList<PhotoLocal>>? {
        var columnIndexData: Int
        var columnIndexDate: Int
        val photoLocalOrigin: MutableList<PhotoLocal> = mutableListOf()
        val reSelection: MutableList<PhotoLocal> = mutableListOf()
        var flowPhotos: Flow<MutableList<PhotoLocal>>? = null

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                columnIndexDate = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)

                val absolutePathOfImage = cursor.getString(columnIndexData)
                val dateOfImage = cursor.getString(columnIndexDate)
                val photoLocal = photo {
                    this.date = dateOfImage
                    this.uri = absolutePathOfImage
                }

                photoLocalOrigin.add(photoLocal)
            }
            cursor.close()

            for (i in photoLocalOrigin.size - 1 downTo -1 + 1) {
                try {
                    reSelection.add(photoLocalOrigin[i])
                    if (i == 0) flowPhotos = flow { emit(reSelection) }
                } catch (e: ConcurrentModificationException) {
                    e.printStackTrace()
                }
            }
        }
        return flowPhotos
    }
}