package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilmListViewModel(
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModel() {

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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

            _films.value = result
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