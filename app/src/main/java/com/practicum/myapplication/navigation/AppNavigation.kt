package com.practicum.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.practicum.myapplication.ui.screens.AboutScreen
import com.practicum.myapplication.ui.screens.FavoritesScreen
import com.practicum.myapplication.ui.screens.FilmDetailsScreen
import com.practicum.myapplication.ui.screens.FilmListScreen
import com.practicum.myapplication.viewmodel.FilmViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val filmViewModel: FilmViewModel = viewModel()
    val items = listOf(
        Screen.Films,
        Screen.Favorites,
        Screen.About
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            when (screen) {
                                Screen.Films -> Icon(
                                    Icons.Default.Home,
                                    contentDescription = "Фильмы"
                                )
                                Screen.Favorites -> Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Избранное"
                                )
                                Screen.About -> Icon(
                                    Icons.Default.Info,
                                    contentDescription = "О приложении"
                                )
                                else -> Unit
                            }
                        },
                        label = {
                            Text(
                                when (screen) {
                                    Screen.Films -> "Фильмы"
                                    Screen.Favorites -> "Избранное"
                                    Screen.About -> "О приложении"
                                    else -> ""
                                }
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Films.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Films.route) {
                FilmListScreen(
                    onFilmClick = { filmId ->
                        navController.navigate(
                            Screen.FilmDetails.createRoute(filmId)
                        )
                    },
                    viewModel = filmViewModel
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onFilmClick = { filmId ->
                        navController.navigate(
                            Screen.FilmDetails.createRoute(filmId)
                        )
                    },
                    viewModel = filmViewModel
                )
            }
            composable(Screen.About.route) {
                AboutScreen()
            }


            composable(Screen.FilmDetails.route) { backStackEntry ->
                val filmId = backStackEntry.arguments?.getString("filmId")

                val filmState = remember(filmId) {
                    mutableStateOf<com.practicum.myapplication.data.Film?>(null)
                }

                LaunchedEffect(filmId) {
                    filmState.value = null

                    if (filmId != null) {
                        filmState.value = filmViewModel.getFilmById(filmId)
                    }
                }

                FilmDetailsScreen(
                    film = filmState.value,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRetryClick = {
                        filmState.value = null
                        if (filmId != null) {
                            filmViewModel.retryLoadFilm(filmId) { film ->
                                filmState.value = film
                            }
                        }
                    },
                    viewModel = filmViewModel
                )
            }
        }
    }
}