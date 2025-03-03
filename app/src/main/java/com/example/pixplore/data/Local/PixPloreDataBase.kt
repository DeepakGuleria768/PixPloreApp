package com.example.pixplore.data.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixplore.data.Local.entity.FavouriteImageEntity
import com.example.pixplore.data.Local.entity.UnsplashImageEntity
import com.example.pixplore.data.Local.entity.UnsplashRemoteKeys

@Database(
entities = [FavouriteImageEntity::class, UnsplashImageEntity::class , UnsplashRemoteKeys::class], // table name that we have
    version = 1, // if we do any change to our entity then we need to update this version
    exportSchema = false
)
abstract class PixPloreDataBase : RoomDatabase()  {
    abstract fun favouriteImageDao() : FavouriteImageDao


    abstract fun editorialFeedDao() : EditorialFeedDao
}