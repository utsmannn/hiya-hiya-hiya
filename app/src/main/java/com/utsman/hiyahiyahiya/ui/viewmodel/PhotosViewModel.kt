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
    var dataWithDivider: LiveData<PagedList<PhotoLocal>> = MutableLiveData()

    private fun configPaged(): PagedList.Config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(true)
        .build()

    fun photos() {
        val factory = PhotosDataFactory(photosRepository, false)
        val paged = LivePagedListBuilder(factory, configPaged())
            .build()
        data = paged
    }

    fun photosWithDivider() {
        logi("hahhh")
        val factory = PhotosDataFactory(photosRepository, true)
        val paged = LivePagedListBuilder(factory, configPaged())
            .build()
        dataWithDivider = paged
    }
}