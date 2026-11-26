package com.practicum.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.myapplication.data.local.entity.FilmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    // Получить все избранные фильмы
    @Query("SELECT * FROM favorite_films ORDER BY title ASC")
    fun getAllFavoriteFilms(): Flow<List<FilmEntity>>

    // Добавить фильм в избранное
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFilm(film: FilmEntity)

    // Удалить фильм из избранного
    @Query("DELETE FROM favorite_films WHERE id = :filmId")
    suspend fun deleteFavoriteFilm(filmId: String)

    // Проверить, находится ли фильм в избранном
        @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE id = :filmId)")
    suspend fun isFavorite(filmId: String): Boolean

    // Получить конкретный фильм
        @Query("SELECT * FROM favorite_films WHERE id = :filmId LIMIT 1")
    suspend fun getFavoriteFilmById(filmId: String): FilmEntity?
}