package com.example.pixplore.Presentation.ProfileScreen

import android.graphics.drawable.Icon
import android.os.Build
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.example.pixplore.Presentation.Component.PixPloreLoadingBar

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileLink: String,
    onBackClick: () -> Unit
) {
    // state for loading bar
    var isLoading by remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Profile") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Box(modifier =  Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            // so here we are going to use android Views
            AndroidView(
                factory = {
                    WebView(context).apply {
                        webViewClient = object : WebViewClient(){
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }
                        }
                        loadUrl(profileLink)
                    }
                }
            )
            if(isLoading){
               CircularProgressIndicator()
            }
        }

    }
}
