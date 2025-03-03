package com.example.pixplore.data.remote

import com.example.pixplore.data.remote.dto.UnsplashImageDto
import com.example.pixplore.data.remote.dto.UnsplashImagesSearchResult
import com.example.pixplore.data.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface unsplashApiService {

    @Headers("Authorization: Client-ID $API_KEY")
    // photos is the end point
    @GET("/photos")
     suspend  fun getEditorialFeedImages(
        @Query("page")  page : Int,
        @Query("per_Page")  perPage : Int
     ) : List<UnsplashImageDto>

     // to search photo

    @Headers("Authorization: Client-ID $API_KEY")
    // search photo  end point
    @GET("/search/photos")
    suspend  fun SearchImage(
        @Query("query")  query : String,
        @Query("page")  page : Int,
        @Query("per_Page")  perPage : Int,

    ) : UnsplashImagesSearchResult

     // create another function for the getting single image with end point Get / photo /:id
     @Headers("Authorization: Client-ID $API_KEY")
     @GET("/photos/{id}")
     suspend fun getImage(
         @Path("id") imageId : String
     ) : UnsplashImageDto// in return we get single image
}