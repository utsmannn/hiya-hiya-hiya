package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.hiyahiyahiya.data.repository.RoomRepository
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel() {
    val room = roomRepository.localRooms()
        .flowOn(Dispatchers.Main)
        .asLiveData(viewModelScope.coroutineContext)

    suspend fun insert(localRoom: LocalRoom) = roomRepository.insert(localRoom)
    suspend fun update(localRoom: LocalRoom) = roomRepository.update(localRoom)
}