package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.repository.FavoriteFilmRepository
import com.practicum.myapplication.domain.usecase.GetFilmByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FilmDetailsViewModel(
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val favoriteFilmRepository: FavoriteFilmRepository
) : ViewModel() {

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> =
    favoriteFilmRepository.observeFavoriteIds()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    private val errorHandler = createErrorHandler(
        onError = { message ->
            _error.value = message
            _isLoading.value = false
        },
        defaultMessage = "Не удалось загрузить информацию о фильме"
    )

    fun loadFilm(id: String) {
        viewModelScope.launch(errorHandler) {
            _isLoading.value = true
            _error.value = null

            val result = getFilmByIdUseCase(id)

            _film.value = result
            _isLoading.value = false
        }
    }

    fun toggleFavorite(film: Film) {
        viewModelScope.launch {
            if(favoriteFilmRepository.isFavorite(film.id)) {
                favoriteFilmRepository.removeFavorite(film.id)
            } else {
                favoriteFilmRepository.addFavorite(film)
            }
        }
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