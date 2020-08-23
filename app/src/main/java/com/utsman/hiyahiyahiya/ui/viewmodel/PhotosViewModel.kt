package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.utsman.hiyahiyahiya.data.repository.PhotosRepository
import kotlinx.coroutines.flow.take

class PhotosViewModel(private val photosRepository: PhotosRepository) : ViewModel() {
    suspend fun allPhotos() = photosRepository.allImagesPath().take(100)
}