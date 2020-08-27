package com.utsman.hiyahiyahiya.database.dao

import androidx.room.*
import com.utsman.hiyahiyahiya.database.entity.LocalStory
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalStoryDao {
    @Query("select * from local_story order by time")
    fun localStories(): Flow<MutableList<LocalStory>>

    @Query("select * from local_story where id = :id")
    fun localStory(id: String?) : LocalStory?

    @Query("select * from local_story where userId = :userId")
    fun localStoryUser(userId: String): LocalStory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localStory: LocalStory)

    @Update
    suspend fun update(localStory: LocalStory)

    @Delete
    suspend fun delete(localStory: LocalStory)
}