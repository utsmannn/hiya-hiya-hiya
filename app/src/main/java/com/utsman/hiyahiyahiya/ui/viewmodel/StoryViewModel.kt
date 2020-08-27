package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.hiyahiyahiya.data.repository.ContactRepository
import com.utsman.hiyahiyahiya.data.repository.StoryRepository
import com.utsman.hiyahiyahiya.database.entity.LocalImageBB
import com.utsman.hiyahiyahiya.database.entity.LocalStory
import com.utsman.hiyahiyahiya.model.utils.toStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class StoryViewModel(private val contactRepository: ContactRepository, private val storyRepository: StoryRepository) : ViewModel() {

    fun stories() = storyRepository.localStories()
        /*.flowOn(Dispatchers.IO)
        .map {
            it.map { l -> l.toStory(contactRepository, storyRepository) }
        }*/
        .flowOn(Dispatchers.Main)
        .asLiveData(viewModelScope.coroutineContext)

    fun story(id: String) = storyRepository.localStory(id)
    suspend fun insert(localStory: LocalStory) = storyRepository.insert(localStory)
    suspend fun update(localStory: LocalStory) = storyRepository.update(localStory)
    suspend fun delete(localStory: LocalStory) = storyRepository.delete(localStory)

    fun imageBBs() = storyRepository.localImageBBs()
        .flowOn(Dispatchers.Main)
        .asLiveData(viewModelScope.coroutineContext)

    fun imageBB(id: String) = storyRepository.localImageBB(id)
    suspend fun insertImageBB(localImageBB: LocalImageBB) = storyRepository.insertImageBB(localImageBB)
    suspend fun updateImageBB(localImageBB: LocalImageBB) = storyRepository.updateImageBB(localImageBB)
    suspend fun deleteImageBB(localImageBB: LocalImageBB) = storyRepository.deleteImageBB(localImageBB)
}