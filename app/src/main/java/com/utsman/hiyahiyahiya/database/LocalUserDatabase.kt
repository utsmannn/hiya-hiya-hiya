package com.utsman.hiyahiyahiya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.utsman.hiyahiyahiya.database.dao.LocalUserDao
import com.utsman.hiyahiyahiya.database.entity.LocalUser

@Database(entities = [LocalUser::class], version = 1)
abstract class LocalUserDatabase : RoomDatabase() {
    abstract fun localUserDao(): LocalUserDao

    companion object {
        @Volatile
        private var instance: LocalUserDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocalUserDatabase::class.java, "local_user_db").build()

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}