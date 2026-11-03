package com.practicum.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.practicum.myapplication.data.Film
import com.practicum.myapplication.data.FilmRepository
import kotlinx.coroutines.flow.StateFlow

class FilmListViewModel : ViewModel() {

    val films: List<Film> = FilmRepository.films
}