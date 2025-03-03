package com.example.pixplore.data.Local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.pixplore.data.Local.entity.FavouriteImageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteImageDao {

    @Query("SELECT * FROM favourite_image_table")
    fun getAllFavouriteImages() : PagingSource<Int,FavouriteImageEntity>
    @Upsert
    suspend fun insertFavouriteImage(image:FavouriteImageEntity)
    @Delete
    suspend fun deleteFavouriteImage(image:FavouriteImageEntity)

    // EXIST check that is this the thing written inside the brackets is exist or not
    @Query("SELECT EXISTS(SELECT 1 FROM favourite_image_table WHERE id = :id)")
    suspend fun isImageFavourite(id:String) : Boolean

    @Query("SELECT id FROM favourite_image_table")
    fun getFavouriteImageId() : Flow<List<String>>
}