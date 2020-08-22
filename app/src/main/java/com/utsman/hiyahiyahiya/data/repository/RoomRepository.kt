package com.utsman.hiyahiyahiya.data.repository

import com.utsman.hiyahiyahiya.database.LocalRoomDatabase
import com.utsman.hiyahiyahiya.database.entity.LocalRoom

class RoomRepository(private val localRoomDatabase: LocalRoomDatabase) {

    fun localRooms() = localRoomDatabase.localRoomDao().localRooms()
    fun localRoom(id: String) = localRoomDatabase.localRoomDao().localRoom(id)
    suspend fun insert(localRoom: LocalRoom) = localRoomDatabase.localRoomDao().insert(localRoom)
    suspend fun update(localRoom: LocalRoom) = localRoomDatabase.localRoomDao().update(localRoom)
    suspend fun delete(localRoom: LocalRoom) = localRoomDatabase.localRoomDao().delete(localRoom)
}