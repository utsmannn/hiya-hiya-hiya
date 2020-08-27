package com.utsman.hiyahiyahiya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.hiyahiyahiya.data.repository.ChatRepository
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import com.utsman.hiyahiyahiya.model.utils.toChat
import com.utsman.hiyahiyahiya.utils.DividerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    fun chats(roomId: String) = chatRepository.localChatsRoom(roomId)
        .map { it.map { c -> c.toChat() } }
        .flowOn(Dispatchers.Main)
        .asLiveData(viewModelScope.coroutineContext)

    suspend fun insert(localChat: LocalChat) = chatRepository.insert(localChat)
    suspend fun update(localChat: LocalChat) = chatRepository.update(localChat)
}