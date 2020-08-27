package com.utsman.hiyahiyahiya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utsman.hiyahiyahiya.database.dao.LocalImageBBDao
import com.utsman.hiyahiyahiya.database.entity.LocalImageBB

@Database(entities = [LocalImageBB::class], version = 1)
@TypeConverters(LocalImageBB.Converter::class)
abstract class LocalImageBBDatabase : RoomDatabase() {
    abstract fun localImageBBDao() : LocalImageBBDao

    companion object {
        @Volatile
        private var instance: LocalImageBBDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocalImageBBDatabase::class.java, "local_imagebb_db").build()

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}