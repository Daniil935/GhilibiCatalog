package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.FavoriteRepository
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModel() {

    val favoriteIds: StateFlow<Set<String>> =
    FavoriteRepository.favoriteIds

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

    fun getFavoriteFilms(): List<Film> {
        return _films.value.filter { it.id in favoriteIds.value }
    }

    fun getReleaseDateText(film: Film): String {
        return "Год выпуска: ${film.releaseDate}"
    }
}