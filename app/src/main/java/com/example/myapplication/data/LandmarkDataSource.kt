package com.example.myapplication.data

import com.example.myapplication.data.model.LandmarkDto
import com.example.myapplication.data.model.LandmarkInfoDto
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LandmarkDataSource @Inject constructor(private val landmarksService: LandmarksService) {
    suspend fun getLandmarksAround(lon: Double, lat: Double): List<LandmarkDto> {
        val result = landmarksService.getLandmarksAround(lon, lat)
        if (result.isSuccessful){
            return result.body() ?: throw HttpException(result)
        } else {
            throw HttpException(result)
        }
    }

    suspend fun getLandmarkInfo(xid: String): LandmarkInfoDto {
        val result = landmarksService.getLandmarkInfo(xid)
        if (result.isSuccessful){
            return result.body() ?: throw HttpException(result)
        } else {
            throw HttpException(result)
        }
    }
}