package com.example.pixplore.Presentation.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.pixplore.data.remote.unsplashApiService
import com.example.pixplore.domain.model.UnSplashImage

@Composable
fun ImageVerticalGrid(
    modifier: Modifier = Modifier,
    favouriteImageId : List<String>,
    images: LazyPagingItems<UnSplashImage>,
    onImageClick: (String) -> Unit,
    onImageDragStart: (UnSplashImage?) -> Unit,
    onImageDragEnd: () -> Unit,
    onTongleFavouriteStatus: (UnSplashImage) -> Unit

) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(count = images.itemCount) { index ->
            val image = images[index]
            ImageCard(
                image = image,
                modifier = modifier
                    .clickable { image?.id?.let { onImageClick(it) } }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { onImageDragStart(image) }, // show what ever image user longpressed
                            onDragCancel = { onImageDragEnd() },
                            onDragEnd = { onImageDragEnd },
                            onDrag = { _, _ -> }
                        )
                    },

                onTongleFavouriteStatus = {
                    image?.let { onTongleFavouriteStatus(it) }
                },
                isFavourite = favouriteImageId.contains(image?.id ),
            )
        }
    }
}

