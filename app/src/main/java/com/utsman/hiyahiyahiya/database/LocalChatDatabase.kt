package com.utsman.hiyahiyahiya.database

import android.content.Context
import androidx.room.*
import com.utsman.hiyahiyahiya.database.dao.LocalChatDao
import com.utsman.hiyahiyahiya.database.entity.LocalChat

@Database(entities = [LocalChat::class], version = 1)
@TypeConverters(LocalChat.Converter::class)
abstract class LocalChatDatabase : RoomDatabase() {
    abstract fun localChatDao(): LocalChatDao

    companion object {
        @Volatile
        private var instance: LocalChatDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocalChatDatabase::class.java, "local_chat_db").build()

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}