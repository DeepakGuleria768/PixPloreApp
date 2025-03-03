package com.example.pixplore.Presentation

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pixplore.Presentation.Component.NetworkStatusBar
import com.example.pixplore.Presentation.Navigation.NavGraphSetup
import com.example.pixplore.Presentation.homescreen.HomeScreen
import com.example.pixplore.Presentation.homescreen.HomeScreenViewModel
import com.example.pixplore.domain.model.NetworkStatus
import com.example.pixplore.domain.repository.NetworkConnectivityObserver
import com.example.pixplore.ui.theme.CustomGreen
import com.example.pixplore.ui.theme.PixPloreTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint // used to annotate android components such as activity , services , fragments , broadCastReceivers
class MainActivity : ComponentActivity() {

    // here we inject the interface
    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver
    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // to add spalsh screen in out application
        installSplashScreen()
        // Status bar color is handled by enableEdgeToEdge() function
        enableEdgeToEdge()
        setContent {

            val status by  connectivityObserver.networkStatus.collectAsState()
            var showMessageBar by rememberSaveable { mutableStateOf(false) }
            var message by rememberSaveable { mutableStateOf("") }
            var backgroundColor by remember { mutableStateOf(Color.Red) }

            LaunchedEffect (key1 = status){
                when(status){
                    NetworkStatus.Connected->
                    {
                        message ="Connected to Internet"
                        backgroundColor = CustomGreen
                        delay(2000)
                        showMessageBar = false
                    }
                    NetworkStatus.Disconnected-> {
                        showMessageBar = true
                        message = "No Internet Connection"
                        backgroundColor = Color.Red
                    }
                }
            }
            PixPloreTheme {

                var searchQuery by rememberSaveable {
                    mutableStateOf("")
                }
                val snackBarstate  = remember{
                    SnackbarHostState()
                }
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                val viewModel = viewModel<HomeScreenViewModel>()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarstate) },
                    modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomBar = {
                      NetworkStatusBar(
                          ShowMessageBar =  showMessageBar,
                          message = message,
                          backgroundColor = backgroundColor
                      )
                    }
                    ) {
                    NavGraphSetup(
                        navController = navController,
                        scrollBehavior = scrollBehavior,
                        snackbarState = snackBarstate,
                        searchQuery = searchQuery,
                        onSearchQueryChange = {searchQuery = it}
                    )
                }
            }
        }
    }
}

