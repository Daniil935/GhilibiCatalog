package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.local.dao.FavoriteFilmDao
import com.practicum.myapplication.data.local.entity.FilmEntity
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.repository.FavoriteFilmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteFilmRepositoryImpl(
    private val dao: FavoriteFilmDao
) : FavoriteFilmRepository {

    override fun observeFavoriteFilms(): Flow<List<Film>> {
        return dao.getAllFavoriteFilms().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeFavoriteIds(): Flow<Set<String>> {
        return dao.getAllFavoriteFilms().map { entities ->
            entities.map { it.id }.toSet()
        }
    }

    override suspend fun addFavorite(film: Film) {
        dao.insertFavoriteFilm(FilmEntity.fromDomain(film))
    }

    override suspend fun removeFavorite(filmId: String) {
        dao.deleteFavoriteFilm(filmId)
    }

    override suspend fun isFavorite(filmId: String): Boolean {
        return dao.isFavorite(filmId)
    }
}