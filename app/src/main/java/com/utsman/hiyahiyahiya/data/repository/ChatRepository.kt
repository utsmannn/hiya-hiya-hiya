package com.utsman.hiyahiyahiya.data.repository

import com.utsman.hiyahiyahiya.database.LocalChatDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalChat

class ChatRepository(private val localChatDatabase: LocalChatDatabase) {

    fun localChats() = localChatDatabase.localChatDao().localChats()
    fun localChatsRoom(roomId: String) = localChatDatabase.localChatDao().localChatsRoom(roomId)
    fun localChat(id: String) = localChatDatabase.localChatDao().localChat(id)
    suspend fun insert(localChat: LocalChat) = localChatDatabase.localChatDao().insert(localChat)
    suspend fun update(localChat: LocalChat) = localChatDatabase.localChatDao().update(localChat)
    suspend fun delete(localChat: LocalChat) = localChatDatabase.localChatDao().delete(localChat)
}