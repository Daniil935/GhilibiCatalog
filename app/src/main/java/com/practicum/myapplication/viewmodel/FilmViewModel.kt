package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.Film
import com.practicum.myapplication.data.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilmViewModel : ViewModel() {

    private val repository = FilmRepository()

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isDetailLoading = MutableStateFlow(false)
    val isDetailLoading: StateFlow<Boolean> =
        _isDetailLoading.asStateFlow()

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> =
        _detailError.asStateFlow()

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getFilms()
                }
                _films.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Не удалось загрузить фильмы"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getFilmById(id: String): Film? {
        _isDetailLoading.value = true
        _detailError.value = null

        return try {
            withContext(Dispatchers.IO) {
                repository.getFilmById(id)
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _detailError.value =
                e.message ?: "Не удалось загрузить информацию о фильме"
            null
        } finally {
            _isDetailLoading.value = false
        }
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
    fun retryLoadFilm(id: String, onResult: (Film?) -> Unit) {
        viewModelScope.launch {
            onResult(getFilmById(id))
        }
    }
}