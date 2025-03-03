package com.example.pixplore.Presentation.FullImageScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.pixplore.Presentation.Navigation.Routes
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.domain.model.UnSplashImage
import com.example.pixplore.domain.repository.Downloader
import com.example.pixplore.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageId = savedStateHandle.toRoute<Routes.FullImageScreen>().imageId

    private val _SnackBarEvent = Channel<SnackBarEvents>()
    val snackBarEvents = _SnackBarEvent.receiveAsFlow()
    var image: UnSplashImage? by mutableStateOf(null)
        private set

    // we are use this getImage function init block because we not have any event to call this function
    init{
        getImage()
    }

    fun getImage() {
        viewModelScope.launch {
            try {
                val result = repository.getImage(imageId)
                image = result
            }catch (e: UnknownHostException) {
                _SnackBarEvent.send(
                    SnackBarEvents(message = "No Internet Connection. Please check your network")
                )
            }

            catch (e: Exception) {
              _SnackBarEvent.send(
                  SnackBarEvents(message = "Something went wrong ${e.message}")
              )
            }
        }
    }

    fun DownlaodImage(url:String,title:String?){
        viewModelScope.launch {
            try {
               downloader.downlaodFile(url,title)
            }catch (e:Exception){
                _SnackBarEvent.send(
                    SnackBarEvents(message = "Something went wrong ${e.message}")
                )
            }
        }
    }
}