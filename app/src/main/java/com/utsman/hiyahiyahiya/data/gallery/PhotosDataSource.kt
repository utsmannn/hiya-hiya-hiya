package com.utsman.hiyahiyahiya.data.gallery

import androidx.paging.PageKeyedDataSource
import com.utsman.hiyahiyahiya.data.repository.PhotosRepository
import com.utsman.hiyahiyahiya.model.features.PhotoLocal
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PhotosDataSource(private val photosRepository: PhotosRepository) : PageKeyedDataSource<Int, PhotoLocal>() {
    private val perPage = 20
    private var page = 0
    private var currentList: MutableList<PhotoLocal> = mutableListOf()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PhotoLocal>) {
        CoroutineScope(Dispatchers.IO).launch {
            photosRepository.allImagesFlowReorder()?.collect {
                currentList = it
                val list = it.subList(page, perPage)
                callback.onResult(list, null, page + perPage + 1)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoLocal>) {
        val nextPage = params.key
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            if (currentList.size > nextPage + perPage) {
                val sizeLoad = nextPage + perPage
                val list = currentList.subList(nextPage, sizeLoad)
                callback.onResult(list, sizeLoad)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoLocal>) {
    }
}