package com.example.pixplore.Presentation.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.domain.model.UnSplashImage
import com.example.pixplore.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {


    private val _snackbarEvent = Channel<SnackBarEvents>()
    val snackBarEvents = _snackbarEvent.receiveAsFlow()


    val images : StateFlow<PagingData<UnSplashImage>> = repository.getEditorialFeedImages()
        .catch {exception->
            _snackbarEvent.send(
                SnackBarEvents(message = "Something went wrong. ${exception.message}")
            )

        }
        .cachedIn(
            viewModelScope
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )

    val favoriteImageIds : StateFlow<List<String>> = repository.getFavouriteImageId()
        .catch {exception->
            _snackbarEvent.send(
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
                _snackbarEvent.send(
                    SnackBarEvents(message = "Something went wrong. ${e.message}")
                )
            }
        }
    }

}
