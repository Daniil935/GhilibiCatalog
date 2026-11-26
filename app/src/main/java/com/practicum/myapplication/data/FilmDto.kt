package com.practicum.myapplication.data

import com.google.gson.annotations.SerializedName
import com.practicum.myapplication.domain.model.Film

data class FilmDto(
    val id: String,
    val title: String,

    @SerializedName("original_title")
    val originalTitle: String,

    val description: String,
    val director: String,
    val producer: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("running_time")
    val runningTime: String,

    @SerializedName("rt_score")
    val rating: String,

    @SerializedName("image")
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
}