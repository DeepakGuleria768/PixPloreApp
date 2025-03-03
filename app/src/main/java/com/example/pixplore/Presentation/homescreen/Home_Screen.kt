package com.example.pixplore.Presentation.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.pixplore.Presentation.Component.ImageVerticalGrid
import com.example.pixplore.Presentation.Component.PixPloreTopAppBar
import com.example.pixplore.Presentation.Component.ZoomedImageCard
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.R
import com.example.pixplore.domain.model.UnSplashImage
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    snackBarEvents: Flow<SnackBarEvents>,
    scrollBehavior: TopAppBarScrollBehavior,
    favouriteImageIds:List<String>,
    onToggleFavouriteStatus : (UnSplashImage) -> Unit,
    image: LazyPagingItems<UnSplashImage>,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFabClick: () -> Unit
) {

    // create state for image preview
    var showImagePreview by remember { mutableStateOf(false) }
    // here we put the image which user is clicked
    var activeImage by remember { mutableStateOf<UnSplashImage?>(null) } // here we keep the unSplashImage

    LaunchedEffect ( key1 = true ){
        snackBarEvents.collect{event->
            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PixPloreTopAppBar(
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick
            )
            ImageVerticalGrid(
                images = image,
                onImageClick = onImageClick,
                onImageDragStart = { image ->
                    activeImage = image
                    showImagePreview = true
                },
                onImageDragEnd = { showImagePreview = false },
                onTongleFavouriteStatus = onToggleFavouriteStatus,
                favouriteImageId = favouriteImageIds
            )
        }

        // here we add floating action button
        FloatingActionButton(onClick = { onFabClick() }, modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)) {
            Icon(
                painter = painterResource(R.drawable.saveicon),
                contentDescription = "null",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        ZoomedImageCard(
            modifier = Modifier,
            image = activeImage ,
            isVisible =showImagePreview
            )
    }
}
