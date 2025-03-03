package com.example.pixplore.data.Paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingDataEvent
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pixplore.data.Local.PixPloreDataBase
import com.example.pixplore.data.Local.entity.UnsplashImageEntity
import com.example.pixplore.data.Local.entity.UnsplashRemoteKeys
import com.example.pixplore.data.mapper.toEntityList
import com.example.pixplore.data.remote.unsplashApiService
import com.example.pixplore.data.util.Constants.ITEM_PER_PAGE
import javax.annotation.meta.When


// With the help of this remote mediator the images on the main screen are there even if the internet is off
// because these images are cashed in the local database with the help of the remote mediator
@OptIn(ExperimentalPagingApi::class)
class EditorialFeedRemoteMediator(
    private val apiService: unsplashApiService,
    private val database: PixPloreDataBase
) : RemoteMediator<Int, UnsplashImageEntity>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    private val editorialFeedDao = database.editorialFeedDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImageEntity>
    ): MediatorResult {
        // Here we specify how RemoteMediator load the data
        try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                   val remoteKeys = getRemoteKeyClosestToCurrentPossition(state)
                    remoteKeys?.nextPage?.minus(1) ?: STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys!=null)
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys!=null)
                    nextPage
                }
            }

            val response =
                apiService.getEditorialFeedImages(page = currentPage, perPage = ITEM_PER_PAGE)
            val endOfPaginationReached  = response.isEmpty()


            val prevPage  = if(currentPage==1) null else currentPage-1
            val nextPage = if(endOfPaginationReached) null else currentPage+1

            // in Room withTransaction function is helpful method for executing a set of database operations with in a single transaction
            // it ensure that all operation perform atomically(mean all operation done completly or non of them do )
            // treat mulltiple operation as a single unit
            database.withTransaction {
                // all these 4 function need to be execute together
                      if(loadType == LoadType.REFRESH){
                          editorialFeedDao.deleteAllEditorialFeedImages()
                          editorialFeedDao.deleteAllRemoteKeys()
                      }
                // provide remote keys
                val remoteKeys = response.map { unsplashImageDto ->
                    UnsplashRemoteKeys(
                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                editorialFeedDao.insertAllRemotekeys(remoteKeys)
                editorialFeedDao.insertEditorialFeedImages(response.toEntityList())
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPossition(
        state: PagingState<Int, UnsplashImageEntity>
    ):UnsplashRemoteKeys?{
        return state.anchorPosition?.let{
            position->
            state.closestItemToPosition(position)?.id?.let{id->
                editorialFeedDao.getRemoteKeys(id = id)
            }
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UnsplashImageEntity>): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                editorialFeedDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private  suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UnsplashImageEntity>): UnsplashRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                editorialFeedDao.getRemoteKeys(id = unsplashImage.id)
            }
    }
}