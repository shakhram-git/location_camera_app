package com.example.myapplication.data

import com.example.myapplication.data.model.PhotoModelDto
import com.example.myapplication.entity.Landmark
import com.example.myapplication.entity.LandmarkInfo
import com.example.myapplication.entity.PhotoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val photoModelDao: PhotoModelDao,
    private val landmarkDataSource: LandmarkDataSource
) {

    fun getAllPhotos(): Flow<List<PhotoModel>> {
        return photoModelDao.getAllPhotos()
    }

    suspend fun addPhoto(photo: PhotoModel) {
        photoModelDao.addPhoto(PhotoModelDto.fromPhotoModel(photo))
    }

    suspend fun getLandmarksAround(lon: Double, lat: Double): List<Landmark> {
        return landmarkDataSource.getLandmarksAround(lon, lat)
    }

    suspend fun getLandmarkInfo(xid: String): LandmarkInfo {
        return landmarkDataSource.getLandmarkInfo(xid)
    }
}