package com.example.pixplore.Presentation.FavouriteScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.pixplore.Presentation.Component.ImageVerticalGrid
import com.example.pixplore.Presentation.Component.PixPloreTopAppBar
import com.example.pixplore.Presentation.Component.ZoomedImageCard
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.Presentation.util.searchKeywords
import com.example.pixplore.R
import com.example.pixplore.domain.model.UnSplashImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun favouiteScreen(
    modifier: Modifier = Modifier,
    favouiteImages: LazyPagingItems<UnSplashImage>,
    onBackClick: () -> Unit,
    favouriteImageIds: List<String>,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit,
    onImageClick: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackBarEvents: Flow<SnackBarEvents>,
    onToggleFavouriteStatus: (UnSplashImage) -> Unit
) {

    // create state for image preview
    var showImagePreview by remember { mutableStateOf(false) }
    // here we put the image which user is clicked
    var activeImage by remember { mutableStateOf<UnSplashImage?>(null) } // here we keep the unSplashImage

    LaunchedEffect(key1 = true) {
        snackBarEvents.collect { event ->
            snackbarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PixPloreTopAppBar(
                title = "Favorite",
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "arrowback"
                        )
                    }
                }
            )

            ImageVerticalGrid(
                images = favouiteImages,
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
        ZoomedImageCard(
            modifier = Modifier,
            image = activeImage,
            isVisible = showImagePreview
        )

        if(favouiteImages.itemCount == 0){
            EmptySpace(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )
        }
    }
}


@Composable
private fun EmptySpace(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(modifier = Modifier.size(100.dp),painter = painterResource(R.drawable.bookmark), contentDescription = null)
        Spacer(Modifier.height(48.dp))
        Text("No Saved Image",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(48.dp))
        Text("Image you save will be stored here",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium)
    }
}