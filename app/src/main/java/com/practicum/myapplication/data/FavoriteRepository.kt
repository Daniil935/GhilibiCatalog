package com.practicum.myapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoriteRepository {

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())

    val favoriteIds: StateFlow<Set<String>> =
    _favoriteIds.asStateFlow()

    fun toggleFavorite(filmId: String) {
        _favoriteIds.value =
            if (filmId in _favoriteIds.value) {
                _favoriteIds.value - filmId
            } else {
                _favoriteIds.value + filmId
            }
    }
}