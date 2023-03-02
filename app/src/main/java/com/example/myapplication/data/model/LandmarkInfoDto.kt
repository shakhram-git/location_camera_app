package com.example.myapplication.data.model

import com.example.myapplication.R
import com.example.myapplication.entity.LandmarkInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LandmarkInfoDto(
    override val xid: String,
    override val name: String,
    val address: Address,
    val rate: String,
    val wikipedia: String?,
    val preview: Preview?,
    @Json(name = "wikipedia_extracts")
    val wikipediaExtracts: WikipediaExtracts?
) : LandmarkInfo {
    @JsonClass(generateAdapter = true)
    data class Address(
        val city: String?,
        val country: String,
        val pedestrian: String?,
        @Json(name = "house_number")
        val houseNumber: String?
    )

    @JsonClass(generateAdapter = true)
    data class Preview(
        val source: String
    )

    @JsonClass(generateAdapter = true)
    data class WikipediaExtracts(
        val text: String
    )

    override val city: String? = address.city
    override val country: String = address.country
    override val street: String? = address.pedestrian
    override val houseNumber: String? = address.houseNumber
    override val imageSource: String = preview?.source ?: ""
    override val text: String = wikipediaExtracts?.text ?: ""
}
