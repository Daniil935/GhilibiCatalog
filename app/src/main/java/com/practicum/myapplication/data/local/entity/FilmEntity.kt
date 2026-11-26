package com.practicum.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.myapplication.domain.model.Film

@Entity(tableName = "favorite_films")
data class FilmEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val originalTitle: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseDate: String,
    val runningTime: String,
    val rating: String,
    val imageUrl: String
) {
    fun toDomain(): Film {
        return Film(
            id = id,
            title = title,
            originalTitle = originalTitle,
            description = description,
            director = director,
            producer = producer,
            releaseDate = releaseDate,
            runningTime = runningTime,
            rating = rating,
            imageUrl = imageUrl
        )
    }

    companion object {
        fun fromDomain(film: Film): FilmEntity {
            return FilmEntity(
                id = film.id,
                title = film.title,
                originalTitle = film.originalTitle,
                description = film.description,
                director = film.director,
                producer = film.producer,
                releaseDate = film.releaseDate,
                runningTime = film.runningTime,
                rating = film.rating,
                imageUrl = film.imageUrl
            )
        }
    }
}