package com.example.pixplore.Presentation.Component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pixplore.domain.model.UnSplashImage

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    image: UnSplashImage?,
    isFavourite: Boolean,
    onTongleFavouriteStatus: () -> Unit
) {

    val ImageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlSmall)
        .crossfade(true)
        .build()
    // calculate the aspect ration of all the image though height and width of the image
    val aspectRation: Float by remember {
        derivedStateOf { (image?.width?.toFloat() ?: 1f) / (image?.height?.toFloat() ?: 1f) }
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRation) // what ever be the width the height will be the same
            .then(modifier)
    )
    {
        Box {
            // we are going to use coil library to load image asynchronously
            AsyncImage(
                model = ImageRequest, contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                modifier = modifier.fillMaxSize()
            )
            FavouriteButton(
                isFavourite = isFavourite,
                onClick = onTongleFavouriteStatus,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }


    }
}

@Composable
fun FavouriteButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClick: () -> Unit
) {

    FilledIconToggleButton(
        modifier = modifier,
        checked = isFavourite,
        onCheckedChange = { onClick() },
        colors = IconButtonDefaults.filledIconToggleButtonColors(
            containerColor = Color.Transparent
        )
    ) {
        if (isFavourite) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        } else {
            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
        }

    }
}


