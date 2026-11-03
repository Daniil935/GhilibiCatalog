package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.practicum.myapplication.data.Film
import com.practicum.myapplication.data.FilmRepository
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel : ViewModel() {

    val favoriteIds: StateFlow<Set<String>> =
    FilmRepository.favoriteIds

    fun getFavoriteFilms(): List<Film> {
        return FilmRepository.getFavoriteFilms()
    }
}