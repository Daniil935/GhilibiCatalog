package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myapplication.data.repository.FilmRepositoryImpl
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmListViewModel : ViewModel() {

    private val repository = FilmRepositoryImpl()
    private val getFilmsUseCase = GetFilmsUseCase(repository)

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadFilms()
    }

    fun loadFilms() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    getFilmsUseCase()
                }

                _films.value = result

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    e.message ?: "Не удалось загрузить фильмы"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDirectorText(film: Film): String {
        return "Режиссёр: ${film.director}"
    }

    fun getRatingAndRunningTimeText(film: Film): String {
        return "★ ${film.rating}%  •  ${film.runningTime} мин."
    }
}