package com.example.pixplore.Presentation.Search_Screen

import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.room.util.query
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
fun searchScreen(
    modifier: Modifier = Modifier,
    searchedImages:LazyPagingItems<UnSplashImage>,
    onBackClick: () -> Unit,
    searchQuery:String,
    favouriteImageIds : List<String>,
    onSearchQueryChange:(String)->Unit,
    onImageClick: (String) -> Unit,
    onSearch: (String)->Unit,
    snackbarHostState: SnackbarHostState,
    snackBarEvents: Flow<SnackBarEvents>,
    onToggleFavouriteStatus: (UnSplashImage) -> Unit
) {

    // state to show chips
    var isSuggestionChipVisible by remember { mutableStateOf(false) }
    val focusRequest = remember { FocusRequester() }


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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

    // we want that out focus is should be on search bar so we use launched effect
    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequest.requestFocus()
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
            // here search screen is comming
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequest)
                    .padding(vertical = 10.dp)
                    .onFocusChanged { isSuggestionChipVisible = it.isFocused },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    IconButton(
                        // we want when we press the cross button then if there is text already
                        // inside the search bar then first it remove that text then close the
                        // search bar
                        onClick = { if (searchQuery.isNotEmpty()) onSearchQueryChange("") else onBackClick() }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                },
                // text that you are writing in search bar
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it)},
                active = false,
                onActiveChange = {},
                onSearch = {
                    onSearch(searchQuery)
                    // here we add the functionality that if the User press the search icon in keyboard then the focus remove

                    // from the search bar to and also keyboard is hide

                    // firstly hide the keyboard
                    keyboardController?.hide()
                    // remove the focus from the search bar
                    focusManager.clearFocus()

                },
                content = { }
            )

            // here we show the chips only when the focus is on search bar so we use animatedVisibility
            // because they are visible or not visible according to change in state
            AnimatedVisibility(
                visible = isSuggestionChipVisible
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchKeywords){keywords->
                        // here suggestion chips comes with material 3
                        SuggestionChip(
                            onClick = {
                                onSearch(keywords)
                                // write the same keyword in search bar which user click in chip
                                onSearchQueryChange(keywords)
                                // again when we click on suggestion chip
                                // we hide the keyboard and remove the focus
                                // firstly hide the keyboard
                                keyboardController?.hide()
                                // remove the focus from the search bar
                                focusManager.clearFocus()


                            },
                            label = {Text(keywords)},
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            ImageVerticalGrid(
                images = searchedImages,
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
    }

}


