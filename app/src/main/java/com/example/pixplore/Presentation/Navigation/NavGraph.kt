package com.example.pixplore.Presentation.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.onFocusedBoundsChanged
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.util.query
import com.example.pixplore.Presentation.FavouriteScreen.FavoriteViewModel
import com.example.pixplore.Presentation.FavouriteScreen.favouiteScreen
import com.example.pixplore.Presentation.FullImageScreen.FullImageViewModel
import com.example.pixplore.Presentation.FullImageScreen.fullImageScreen
import com.example.pixplore.Presentation.ProfileScreen.ProfileScreen
import com.example.pixplore.Presentation.Search_Screen.SearchViewModel
import com.example.pixplore.Presentation.Search_Screen.searchScreen
import com.example.pixplore.Presentation.homescreen.HomeScreen
import com.example.pixplore.Presentation.homescreen.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarState: SnackbarHostState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.HomeScreen) {

        composable<Routes.HomeScreen> {
            val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
            val images = homeScreenViewModel.images.collectAsLazyPagingItems()
            val favoriteImageIds by homeScreenViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            HomeScreen(
                snackBarEvents = homeScreenViewModel.snackBarEvents,
                snackbarHostState = snackbarState,
                scrollBehavior = scrollBehavior,
                image = images,
                favouriteImageIds = favoriteImageIds,
                onImageClick = { imageid ->
                    navController.navigate(Routes.FullImageScreen(imageid))
                },
                onSearchClick = {
                    // onSearchClick logic here
                    navController.navigate(Routes.SearchScreen)
                },
                // When user Click on the fab button then user dirrected to the Favourite Screen .
                onFabClick = { navController.navigate(Routes.FavouriteScreen) },
                onToggleFavouriteStatus = {homeScreenViewModel.toggleFavouriteStatus(it)})
        }

        composable<Routes.SearchScreen> {
            val searchViewModel: SearchViewModel = hiltViewModel()
            val searchedImages = searchViewModel.searchImage.collectAsLazyPagingItems()
            val favouriteImageIds by searchViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            searchScreen(
                onBackClick = { navController.navigateUp() },
                snackbarHostState = snackbarState,
                searchedImages = searchedImages,
                searchQuery = searchQuery,
                favouriteImageIds = favouriteImageIds,
                onSearchQueryChange = onSearchQueryChange,
                snackBarEvents = searchViewModel.snackBarEvents,
                onImageClick = { imageid ->
                    navController.navigate(Routes.FullImageScreen(imageid))
                },
                onSearch = { searchViewModel.searchImage(it) },
                onToggleFavouriteStatus = { searchViewModel.toggleFavouriteStatus(it) }
            )
        }

        composable<Routes.FavouriteScreen> {
            val favoriteViewModel: FavoriteViewModel = hiltViewModel()
            val favoriteImages = favoriteViewModel.favoriteImages.collectAsLazyPagingItems()
            val favoriteImageIds by favoriteViewModel.favoriteImageIds.collectAsStateWithLifecycle()


            favouiteScreen(
                favouiteImages = favoriteImages,
                onBackClick = { navController.navigateUp() },
                favouriteImageIds = favoriteImageIds,
                scrollBehavior = scrollBehavior,
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                onImageClick = { imageID ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageID))
                },
                snackbarHostState = snackbarState,
                snackBarEvents = favoriteViewModel.snackBarEvents,
                onToggleFavouriteStatus = { favoriteViewModel.toggleFavouriteStatus(it) }
            )
        }

        composable<Routes.FullImageScreen> {
            val fullImageViewModel: FullImageViewModel = hiltViewModel()
            fullImageScreen(image = fullImageViewModel.image,
                snackBarEvents = fullImageViewModel.snackBarEvents,
                snackBarState = snackbarState,
                onBackClick = { navController.navigateUp() },
                onPhotoGrapherNameClick = { profileLink ->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownlaodClick = { url, title ->
                    fullImageViewModel.DownlaodImage(url, title)
                }
            )
        }

        composable<Routes.ProfileScreen> { backStackEntry ->
            val profileLink = backStackEntry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}