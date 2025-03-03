package com.example.pixplore.Presentation.FullImageScreen

import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.pixplore.Presentation.Component.FullImageViewTopBar
import com.example.pixplore.Presentation.Component.ImageDownloadingOptions
import com.example.pixplore.Presentation.Component.PixPloreLoadingBar
import com.example.pixplore.Presentation.Component.downloadOptionsBottomSheet
import com.example.pixplore.Presentation.util.SnackBarEvents
import com.example.pixplore.R
import com.example.pixplore.domain.model.UnSplashImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun fullImageScreen(
    image: UnSplashImage?,
    snackBarEvents: Flow<SnackBarEvents>,
    snackBarState: SnackbarHostState,
    onPhotoGrapherNameClick: (String)->Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onImageDownlaodClick: (String,String?)->Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
val sheetState  = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isDownlaodBottomSheetOpen by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        snackBarEvents.collect{event->
            snackBarState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }


    downloadOptionsBottomSheet(
        isOpen = isDownlaodBottomSheetOpen,
        sheetState = sheetState,
        onDismissRequest ={isDownlaodBottomSheetOpen = false} ,
        onOptionClick = {option ->
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if(!sheetState.isVisible) isDownlaodBottomSheetOpen = false
            }
             val url = when(option){
              ImageDownloadingOptions.SMALL->   image?.imageUrlSmall
              ImageDownloadingOptions.MEDIUM ->image?.imageUrlRegular
              ImageDownloadingOptions.LARGE ->image?.imageUrlRaw
}
            // by this url we first check is it not null only then we download
            url?.let{
                onImageDownlaodClick(it,image?.description?.take(20))
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Box(modifier = modifier.fillMaxSize(),) {


        BoxWithConstraints(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center// to show the loading bar in center
        ) {

            var scale by remember { mutableFloatStateOf(1f) } // 1f mean the what ever the initial size of the image(actual size of the image)
            var offset by remember { mutableStateOf(Offset.Zero) } // initial will be Zero
            // check iamge is already zoomed or not or in its initial state
            val isImageZoomed: Boolean by remember { derivedStateOf { scale != 1f } }  // it means image is zoomed if scale != 1f
            // zoom state
            val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                // this mean the scale value not go below 1f
                scale = max(scale * zoomChange, 1f)
                // here we add login so that if we drag the image the image not leaving its area and not going outside the screen
                // by getting max y cordinate or max x cordinate
                val maxY = (constraints.maxHeight * (scale - 1)) / 2
                val maxX = (constraints.maxWidth * (scale - 1)) / 2
                offset = Offset(

                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                )
            }
            var isLoading by remember { mutableStateOf(true) }
            var isError by remember { mutableStateOf(false) }
            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = { imageState ->
                    isLoading = imageState is AsyncImagePainter.State.Loading
                    isError = imageState is AsyncImagePainter.State.Error
                }
            )

            if (isLoading) {
                PixPloreLoadingBar()
            }
            Image(
                painter = if (isError.not()) imageLoader else painterResource(R.drawable.error),
                contentDescription = null,
                // here we use the tranflormable state
                modifier = modifier
                    .fillMaxSize()
                    // if user double tap on image then the image automatically zoom in
                    .combinedClickable(
                        onDoubleClick = {
                            if (isImageZoomed) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scope.launch {
                                    transformState.animateZoomBy(zoomFactor = 3f)
                                }

                            }
                        },
                        onClick = {},
                        // to remove the riple effect or clickable effect
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }

                    )
                    .transformable(transformState)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )
        }

        FullImageViewTopBar(
            modifier = modifier
                // yaha fill maxwidth and padding top add kiya hai
                .align(Alignment.TopCenter)
                .padding(horizontal = 5.dp, vertical = 40.dp),
            image = image,
            onBackClick = onBackClick,
            onPhotoGrapherNameClick = onPhotoGrapherNameClick,
            onDownloadImageClick = {isDownlaodBottomSheetOpen = true}
        )
    }

}


