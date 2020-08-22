package com.utsman.hiyahiyahiya.database.dao

import androidx.room.*
import com.utsman.hiyahiyahiya.database.entity.LocalRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalRoomDao {
    @Query("select * from local_room order by lastDate")
    fun localRooms(): Flow<MutableList<LocalRoom>>

    @Query("select * from local_room where id = :id")
    fun localRoom(id: String?) : LocalRoom?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localRoom: LocalRoom)

    @Update
    suspend fun update(localRoom: LocalRoom)

    @Delete
    suspend fun delete(localRoom: LocalRoom)
}