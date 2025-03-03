package com.example.pixplore.Presentation.Search_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.domain.model.UnSplashImage
import com.example.pixplore.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ServiceComponent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ImageRepository
): ViewModel() {

    private val _SnackBar = Channel<SnackBarEvents>()
    val snackBarEvents = _SnackBar.receiveAsFlow()

    private val _searchImages = MutableStateFlow<PagingData<UnSplashImage>>(PagingData.empty())
    val searchImage = _searchImages

    fun searchImage(query:String){
        viewModelScope.launch {
            try{
                   repository
                       .searchImage(query)
                       .cachedIn(viewModelScope)
                       .collect{_searchImages.value = it}
            }catch (e:Exception){
                _SnackBar.send(
                    SnackBarEvents(message = "Something went wrong. ${e.message}")
                )
            }
        }
    }

    val favoriteImageIds : StateFlow<List<String>> = repository.getFavouriteImageId()
        .catch {exception->
            _SnackBar.send(
                SnackBarEvents(message = "Something went wrong. ${exception.message}")
            )

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavouriteStatus(image: UnSplashImage){
        viewModelScope.launch{
            try {
                repository.toggleFavoriteStatus(image)
            }catch(e:Exception){
                _SnackBar.send(
                    SnackBarEvents(message = "Something went wrong. ${e.message}")
                )
            }
        }
    }

}