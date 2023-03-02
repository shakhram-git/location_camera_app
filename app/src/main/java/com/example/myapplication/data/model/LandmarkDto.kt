package com.example.myapplication.data.model


import com.example.myapplication.entity.Landmark
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandmarkDto(
    override val xid: String,
    override val name: String,
    val dist: Double,
    override val rate: Int,
    val point: Point
): Landmark{
    override val distance: Int = dist.toInt()
    override val lon: Double = point.lon
    override val lat: Double = point.lat

    @JsonClass(generateAdapter = true)
    data class Point (
        val lon: Double,
        val lat: Double
    )
}