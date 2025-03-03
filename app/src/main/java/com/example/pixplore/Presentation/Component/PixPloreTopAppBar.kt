package com.example.pixplore.Presentation.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pixplore.R
import com.example.pixplore.domain.model.UnSplashImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixPloreTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    title: String = "Pix  Plore", // space is important here for the split function use in the title
    // for on click functionality for the icon we pass in the parameter
    onSearchClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(title.split(" ").first())
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append(" ${title.split("  ").last()}")
                    }
                }, fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.sriracharegular))
            )
        },
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
        navigationIcon =  navigationIcon
    )
}

@Composable
fun FullImageViewTopBar(
    modifier: Modifier = Modifier,
    image : UnSplashImage?,
    onPhotoGrapherNameClick: (String)->Unit,
    onBackClick: () -> Unit,
    onDownloadImageClick: ()->Unit,
) {
    Row(

        modifier = modifier
        ,verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "arrowback"
            )
        }

        // here the user image profile coming
        AsyncImage(
            modifier = modifier.size(40.dp).clip(CircleShape),
            model = image?.photographerProfileImageUrl,
            contentDescription = null
        )

        Spacer(modifier.width(10.dp))

        Column(
            modifier = modifier.clickable {
                image?.let {  onPhotoGrapherNameClick(it.photographerProfileLink) }
                }
        ) {
            Text(
                text = image?.photographerName ?: "",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = image?.photographerUserName ?: "",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {onDownloadImageClick()}) {
            Icon(painter = painterResource(R.drawable.download),
                contentDescription = "null",
                tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}


