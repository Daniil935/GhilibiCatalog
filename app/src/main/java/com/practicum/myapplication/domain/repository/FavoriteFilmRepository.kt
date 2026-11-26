package com.practicum.myapplication.domain.repository

import com.practicum.myapplication.domain.model.Film
import kotlinx.coroutines.flow.Flow

interface FavoriteFilmRepository {

    // Получить список избранных фильмов
    fun observeFavoriteFilms(): Flow<List<Film>>

    // Получить ID всех избранных фильмов
    fun observeFavoriteIds(): Flow<Set<String>>

    // Добавить фильм в избранное
    suspend fun addFavorite(film: Film)

    // Удалить фильм из избранного
    suspend fun removeFavorite(filmId: String)

    // Проверить, находится ли фильм в избранном
    suspend fun isFavorite(filmId: String): Boolean
}