package com.utsman.hiyahiyahiya.database.dao

import androidx.room.*
import com.utsman.hiyahiyahiya.database.entity.LocalImageBB
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalImageBBDao {

    @Query("select * from local_image_bb")
    fun localImageBBs(): Flow<List<LocalImageBB>>

    @Query("select * from local_image_bb")
    fun localImageBBList(): List<LocalImageBB>

    @Query("select * from local_image_bb where id = :id")
    fun localImageBB(id: String): LocalImageBB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localImageBB: LocalImageBB)

    @Update
    suspend fun update(localImageBB: LocalImageBB)

    @Delete
    suspend fun delete(localImageBB: LocalImageBB)
}