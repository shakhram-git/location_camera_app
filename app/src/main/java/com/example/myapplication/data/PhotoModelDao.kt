package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.PhotoModelDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoModelDao {
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoModelDto>>

    @Insert
    suspend fun addPhoto(photo: PhotoModelDto)
}