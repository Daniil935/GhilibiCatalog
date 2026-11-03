package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.practicum.myapplication.data.Film
import com.practicum.myapplication.data.FilmRepository
import kotlinx.coroutines.flow.StateFlow

class FilmDetailsViewModel : ViewModel() {

    val favoriteIds: StateFlow<Set<String>> =
    FilmRepository.favoriteIds

    fun getFilmById(id: String): Film? {
        return FilmRepository.getFilmById(id)
    }

    fun toggleFavorite(filmId: String) {
        FilmRepository.toggleFavorite(filmId)
    }

    fun getReleaseDateText(film: Film): String {
        return "Год выпуска: ${film.releaseDate}"
    }

    fun getRunningTimeText(film: Film): String {
        return "Продолжительность: ${film.runningTime} мин."
    }

    fun getRatingText(film: Film): String {
        return "Рейтинг: ${film.rating}%"
    }
}