package com.example.pixplore.Presentation.Component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.pixplore.R
import com.example.pixplore.domain.model.UnSplashImage
import com.skydoves.cloudy.Cloudy

@Composable
fun ZoomedImageCard(modifier: Modifier = Modifier, image: UnSplashImage?, isVisible: Boolean) {

    // image request
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlRegular)
        .crossfade(true)
        .placeholderMemoryCacheKey(MemoryCache.Key(image?.imageUrlSmall ?: ""))
        .build()

    Box(
        modifier = modifier.fillMaxWidth().padding(20.dp),
        contentAlignment = Alignment.Center
    ){


        // know we use this box if user click the specific image then that image is open and rest of the surrounding of that image become blur
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(isVisible){
                Cloudy (modifier = modifier.fillMaxSize(), radius = 25){}
            }
            // this card is only visible when user  is going to click on it  so we use here AnimatedVisibility()
            AnimatedVisibility(
                visible = isVisible,
                // know give enter and exit animation
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Card(
                    modifier = modifier
                ) {
                    // know we need a header above image so that we can get the photographer name and its profile
                    Row(
                        modifier = modifier.fillMaxWidth().height(70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = modifier.padding(10.dp).clip(CircleShape).size(35.dp),
                            model = image?.photographerProfileImageUrl,
                            contentDescription = "null"
                        )
                        Text(
                            text = image?.photographerName ?: "Anonymous",
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = FontFamily(Font(R.font.sriracharegular))

                        )

                    }
                    AsyncImage(
                        modifier = modifier.fillMaxWidth(),
                        model = imageRequest,
                        contentDescription = "null"
                    )
                }
            }
        }
    }
}