package com.practicum.myapplication.domain.model

data class Film(
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
)