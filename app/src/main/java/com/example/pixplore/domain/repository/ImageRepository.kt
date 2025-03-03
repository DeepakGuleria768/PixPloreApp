package com.example.pixplore.domain.repository

import androidx.paging.PagingData
import com.example.pixplore.domain.model.UnSplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository{
         fun getEditorialFeedImages(): Flow<PagingData<UnSplashImage>>

        suspend fun getImage(imageId:String):UnSplashImage

       fun searchImage(query:String) : Flow<PagingData<UnSplashImage>>

    fun GetAllFavoriteImages() : Flow<PagingData<UnSplashImage>>

       suspend fun  toggleFavoriteStatus(image:UnSplashImage)

       fun getFavouriteImageId() : Flow<List<String>>



}