package com.utsman.hiyahiyahiya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utsman.hiyahiyahiya.database.dao.LocalRoomDao
import com.utsman.hiyahiyahiya.database.entity.LocalRoom

@Database(entities = [LocalRoom::class], version = 1)
@TypeConverters(LocalRoom.Converter::class)
abstract class LocalRoomDatabase : RoomDatabase() {
    abstract fun localRoomDao(): LocalRoomDao

    companion object {
        @Volatile
        private var instance: LocalRoomDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocalRoomDatabase::class.java, "local_room_db").build()

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}