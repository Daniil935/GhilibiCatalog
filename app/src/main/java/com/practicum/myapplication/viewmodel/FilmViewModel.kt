package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.practicum.myapplication.data.Film
import com.practicum.myapplication.data.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilmViewModel : ViewModel() {

    private val _films = MutableStateFlow(FilmRepository.films)
    val films: StateFlow<List<Film>> = _films.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    fun getFilmById(id: String): Film? {
        return FilmRepository.getFilmById(id)
    }

    fun isFavorite(filmId: String): Boolean {
        return filmId in _favoriteIds.value
    }

    fun toggleFavorite(filmId: String) {
        _favoriteIds.value =
            if (filmId in _favoriteIds.value) {
                _favoriteIds.value - filmId
            } else {
                _favoriteIds.value + filmId
            }
    }

    fun getFavoriteFilms(): List<Film> {
        return _films.value.filter { it.id in _favoriteIds.value }
    }
}