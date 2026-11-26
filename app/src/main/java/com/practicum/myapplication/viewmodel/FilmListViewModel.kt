package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.cache.AppCache
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.model.FilterSettings
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FilmListViewModel(
    private val getFilmsUseCase: GetFilmsUseCase,
    private val appCache: AppCache
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val films: StateFlow<List<Film>> = combine(
    appCache.films,
    appCache.filterSettings
    ) { allFilms, settings ->
        allFilms.filter { film ->
            (settings.title.isBlank() ||
                    film.title.contains(settings.title, ignoreCase = true)) &&
                    (settings.director.isBlank() ||
                            film.director.contains(settings.director, ignoreCase = true)) &&
                    (settings.releaseYear.isBlank() ||
                            film.releaseDate == settings.releaseYear)
        }
    }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
    )
    val isFilterActive: StateFlow<Boolean> = appCache.filterSettings
        .map { settings ->
            settings.title.isNotBlank() ||
                    settings.director.isNotBlank() ||
                    settings.releaseYear.isNotBlank()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )
    private val errorHandler = createErrorHandler(
        onError = { message ->
            _error.value = message
            _isLoading.value = false
        },
        defaultMessage = "Не удалось загрузить фильмы"
    )

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch(errorHandler) {
            _isLoading.value = true
            _error.value = null

            val result = getFilmsUseCase()

            appCache.updateFilms(result)
            _isLoading.value = false
        }
    }

    fun getDirectorText(film: Film): String {
        return "Режиссёр: ${film.director}"
    }

    fun getRatingAndRunningTimeText(film: Film): String {
        return "★ ${film.rating}%  •  ${film.runningTime} мин."
    }
}