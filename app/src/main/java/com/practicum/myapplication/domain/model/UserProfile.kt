package com.practicum.myapplication.domain.model

data class UserProfile(
    val fullName: String = "",
    val avatarUri: String = "",
    val portfolioUrl: String = "",
    val favoriteFilm: String = "",
    val favoriteDirector: String = ""
)