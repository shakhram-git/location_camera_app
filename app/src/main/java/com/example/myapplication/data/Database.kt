package com.example.myapplication.data

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.myapplication.data.model.PhotoModelDto

@Database(
    entities = [PhotoModelDto::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun photoModelDao(): PhotoModelDao
}