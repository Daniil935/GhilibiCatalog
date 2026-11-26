package com.practicum.myapplication.data.cache

import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.model.FilterSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppCache {

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films.asStateFlow()

    private val _filterSettings = MutableStateFlow(FilterSettings())
    val filterSettings: StateFlow<FilterSettings> = _filterSettings.asStateFlow()

    fun updateFilms(films: List<Film>) {
        _films.value = films
    }

    fun updateFilterSettings(settings: FilterSettings) {
        _filterSettings.value = settings
    }
}