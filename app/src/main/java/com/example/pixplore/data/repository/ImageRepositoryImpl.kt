package com.example.pixplore.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pixplore.data.Local.FavouriteImageDao
import com.example.pixplore.data.Local.PixPloreDataBase
import com.example.pixplore.data.Paging.EditorialFeedRemoteMediator
import com.example.pixplore.data.Paging.SearchPagingSource
import com.example.pixplore.data.mapper.toDomainModel
import com.example.pixplore.data.mapper.toDomainModelList
import com.example.pixplore.data.mapper.toFavouriteImageEntity
import com.example.pixplore.data.remote.dto.UnsplashImageDto
import com.example.pixplore.data.remote.unsplashApiService
import com.example.pixplore.data.util.Constants.ITEM_PER_PAGE
import com.example.pixplore.domain.model.UnSplashImage
import com.example.pixplore.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
// object of unsplashApi
    private val unSplashApi: unsplashApiService,
    // provide data base through constructor
    private val database : PixPloreDataBase
) : ImageRepository {

    private val favoriteImageDao = database.favouriteImageDao()
    private val editorialImageDao = database.editorialFeedDao()


    override fun getEditorialFeedImages(): Flow<PagingData<UnSplashImage>> {
       //Attach our function here
        return  return Pager(
            config = PagingConfig(pageSize = ITEM_PER_PAGE) ,
            remoteMediator = EditorialFeedRemoteMediator(unSplashApi,database),
            pagingSourceFactory ={ editorialImageDao.getAllEditorialFeedImages() }
        ).flow // this will convert our pager to flow
            .map { pagingData->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun getImage(imageId: String): UnSplashImage {
        return unSplashApi.getImage(imageId).toDomainModel()
    }

    override fun searchImage(query: String): Flow<PagingData<UnSplashImage>> {
              return Pager(
                  config = PagingConfig(pageSize = ITEM_PER_PAGE) ,
                  pagingSourceFactory ={ SearchPagingSource(query,unSplashApi) }
              ).flow // this will convert our pager to flow
    }

    override fun GetAllFavoriteImages(): Flow<PagingData<UnSplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEM_PER_PAGE) ,
            pagingSourceFactory ={ favoriteImageDao.getAllFavouriteImages() }
        ).flow // this will convert our pager to flow

            .map { pagingData->
                pagingData.map { it.toDomainModel() }

            }
    }


    // Function for Toggling the favourite screen

    override suspend fun toggleFavoriteStatus(image: UnSplashImage) {

        val FavouriteImage = image.toFavouriteImageEntity()
        val isFavorite = favoriteImageDao.isImageFavourite(image.id)
        if(isFavorite){
            favoriteImageDao.deleteFavouriteImage(FavouriteImage)
        }else{
            favoriteImageDao.insertFavouriteImage(FavouriteImage)
        }

    }

    override fun getFavouriteImageId(): Flow<List<String>> {
        return favoriteImageDao.getFavouriteImageId()
    }
}

