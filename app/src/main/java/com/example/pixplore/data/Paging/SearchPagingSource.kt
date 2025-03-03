package com.example.pixplore.data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixplore.data.remote.unsplashApiService
import com.example.pixplore.domain.model.UnSplashImage
import com.example.pixplore.data.mapper.toDomainModelList

class SearchPagingSource(
    private val query : String,
    private val UnsplashApi : unsplashApiService

) : PagingSource<Int,UnSplashImage>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, UnSplashImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashImage> {
     val current_pager =   params.key?: STARTING_PAGE_INDEX
           return try{
             val response =  UnsplashApi.SearchImage(
                   query = query,
                   page = current_pager ,
                   perPage = params.loadSize // params means provide through parameters
               )
               val endOfPaginationReached = response.images.isEmpty()
               LoadResult.Page(
                   data  = response.images.toDomainModelList(),
                   prevKey = if(current_pager== STARTING_PAGE_INDEX) null else current_pager-1 ,
                   nextKey = if(endOfPaginationReached) null else current_pager+1
               )
           }catch(e:Exception){
               LoadResult.Error(e)
           }
    }

}