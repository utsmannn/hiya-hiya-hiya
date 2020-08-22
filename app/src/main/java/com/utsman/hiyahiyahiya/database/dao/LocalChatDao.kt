package com.utsman.hiyahiyahiya.database.dao

import androidx.room.*
import com.utsman.hiyahiyahiya.database.entity.LocalChat
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalChatDao {
    @Query("select * from local_chat order by time")
    fun localChats(): Flow<MutableList<LocalChat>>

    @Query("select * from local_chat where roomId = :roomId order by time")
    fun localChatsRoom(roomId: String) : Flow<MutableList<LocalChat>>

    @Query("select * from local_chat where id = :id")
    fun localChat(id: String) : Flow<LocalChat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localChat: LocalChat)

    @Update
    suspend fun update(localChat: LocalChat)

    @Delete
    suspend fun delete(localChat: LocalChat)
}