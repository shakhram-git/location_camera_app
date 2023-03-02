package com.example.myapplication.data

import com.example.myapplication.data.model.LandmarkDto
import com.example.myapplication.data.model.LandmarkInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LandmarksService {
    @GET("/0.1/en/places/radius?radius=12000&limit=50&rate=1h&format=json&apikey=5ae2e3f221c38a28845f05b60324b40127f77fd1ac02ffdbc562ab75")
    suspend fun getLandmarksAround(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double
    ): Response<List<LandmarkDto>>

    @GET("/0.1/en/places/xid/{xid}?apikey=5ae2e3f221c38a28845f05b60324b40127f77fd1ac02ffdbc562ab75")
    suspend fun getLandmarkInfo(
        @Path("xid") xid: String
    ): Response<LandmarkInfoDto>
}