package com.example.pixplore.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable // kotlin Serialization will use to deserialize the json data to our usable object
data class UnsplashImageDto(
    val id: String,
    val description: String?, // null because some time discriptions can be null also
    val height: Int,
    val width: Int,
    val urls: Urls,
    val user: UserDto,
)

@Serializable
data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)