package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.FavoriteRepository
import com.practicum.myapplication.data.repository.FilmRepositoryImpl
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.usecase.GetFilmByIdUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmDetailsViewModel : ViewModel() {

    private val repository = FilmRepositoryImpl()
    private val getFilmByIdUseCase = GetFilmByIdUseCase(repository)

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> =
    FavoriteRepository.favoriteIds

    fun loadFilm(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    getFilmByIdUseCase(id)
                }

                _film.value = result

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    e.message ?: "Не удалось загрузить информацию о фильме"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(filmId: String) {
        FavoriteRepository.toggleFavorite(filmId)
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