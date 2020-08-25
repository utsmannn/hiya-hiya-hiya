package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.utsman.hiyahiyahiya.data.gallery.PhotosDataFactory
import com.utsman.hiyahiyahiya.data.repository.PhotosRepository
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.model.row.RowImage
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class PhotosViewModel(private val photosRepository: PhotosRepository) : ViewModel() {

    var data: LiveData<PagedList<PhotoLocal>> = MutableLiveData()

    suspend fun allPhotos() = photosRepository.allImagesPath().take(100)

    suspend fun allPhotosReorder() = photosRepository
        .allImagesFlowReorder()?.map { r ->
            logi("dateeee -> ${r.first().date.toLong()*1000}")
            r.map { p -> RowImage.Item1(p.uri) }
        }

    fun getAllPhotos() = photosRepository.allPhotosReorder()

    private fun configPaged(): PagedList.Config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(true)
        .build()

    fun photos() {
        val factory = PhotosDataFactory(photosRepository)
        val paged = LivePagedListBuilder(factory, configPaged())
            .build()
        data = paged
    }
}