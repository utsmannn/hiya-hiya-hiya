package com.utsman.hiyahiyahiya.data.gallery

import androidx.paging.DataSource
import com.utsman.hiyahiyahiya.data.repository.PhotosRepository
import com.utsman.hiyahiyahiya.model.features.PhotoLocal

class PhotosDataFactory(private val photosRepository: PhotosRepository) : DataSource.Factory<Int, PhotoLocal>() {
    override fun create(): DataSource<Int, PhotoLocal> {
        return PhotosDataSource(photosRepository)
    }
}