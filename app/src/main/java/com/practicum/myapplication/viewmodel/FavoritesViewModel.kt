package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.repository.FavoriteFilmRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toSet

class FavoritesViewModel(
    private val favoriteFilmRepository: FavoriteFilmRepository
) : ViewModel() {

    // Список избранных фильмов из Room
    val films: StateFlow<List<Film>> =
    favoriteFilmRepository.observeFavoriteFilms()
    .stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
    )

    // ID избранных фильмов
    val favoriteIds: StateFlow<Set<String>> =
    films.map { favoriteFilms ->
        favoriteFilms.map { it.id }.toSet()
    }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptySet()
    )

    // Состояния для экрана
    val isLoading: StateFlow<Boolean> =
        kotlinx.coroutines.flow.MutableStateFlow(false)

    val error: StateFlow<String?> =
        kotlinx.coroutines.flow.MutableStateFlow(null)

    fun getFavoriteFilms(): List<Film> {
        return films.value
    }

    fun getReleaseDateText(film: Film): String {
        return "Год выпуска: ${film.releaseDate}"
    }
}