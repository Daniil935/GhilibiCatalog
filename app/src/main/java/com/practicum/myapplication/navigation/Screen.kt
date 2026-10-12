package com.practicum.myapplication.navigation

sealed class Screen(val route: String) {
    data object Films : Screen("films")
    data object Favorites : Screen("favorites")
    data object About : Screen("about")

    data object FilmDetails : Screen("film_details/{filmId}") {
        fun createRoute(filmId: String): String {
            return "film_details/$filmId"
        }
    }
}