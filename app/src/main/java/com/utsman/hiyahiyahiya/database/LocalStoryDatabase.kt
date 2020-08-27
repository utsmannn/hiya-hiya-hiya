package com.utsman.hiyahiyahiya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utsman.hiyahiyahiya.database.dao.LocalStoryDao
import com.utsman.hiyahiyahiya.database.entity.LocalStory

@Database(entities = [LocalStory::class], version = 1)
@TypeConverters(LocalStory.Converter::class)
abstract class LocalStoryDatabase : RoomDatabase() {
    abstract fun localStoryDao(): LocalStoryDao

    companion object {
        @Volatile
        private var instance: LocalStoryDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocalStoryDatabase::class.java, "local_story_db").build()

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}