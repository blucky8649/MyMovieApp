package com.example.mymovieapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymovieapp.model.Keyword


@Database(
    entities = [Keyword::class],
    version = 3
)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun getHistoryDao(): HistoryDao
}