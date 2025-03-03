package com.example.pixplore.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImagesSearchResult(
    @SerialName("results")
    val images: List<UnsplashImageDto>,
    val total: Int, // total number of images
    @SerialName("total_pages")
    val totalPages: Int // total number of pages
)