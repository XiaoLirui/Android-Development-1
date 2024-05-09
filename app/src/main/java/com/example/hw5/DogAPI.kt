package com.example.hw5

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogApiResponse(
//    @Json(name = "message") val images: String,
    val status: String,
//    val message: StringList<String>
//    val message: MutableList<String>
    val message: List<String>
)
