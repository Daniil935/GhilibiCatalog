package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.Film

interface FilmRepository {

    suspend fun getFilms(): List<Film>

    suspend fun getFilmById(id: String): Film
}