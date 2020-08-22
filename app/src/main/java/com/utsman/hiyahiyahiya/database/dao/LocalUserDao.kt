package com.utsman.hiyahiyahiya.database.dao

import androidx.room.*
import com.utsman.hiyahiyahiya.database.entity.LocalUser
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {
    @Query("select * from local_user order by name")
    fun localUsers(): Flow<MutableList<LocalUser>>

    @Query("select * from local_user where id = :id")
    fun localUser(id: String) : LocalUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localUser: LocalUser)

    @Update
    suspend fun update(localUser: LocalUser)

    @Delete
    suspend fun delete(localUser: LocalUser)
}