package com.practicum.myapplication.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FilmRepository {

    val films = listOf(
        Film(
            id = "1",
            title = "Унесённые призраками",
            originalTitle = "千と千尋の神隠し",
            description = "История девочки Тихиро, которая попадает в загадочный мир духов и пытается спасти своих родителей.",
            director = "Хаяо Миядзаки",
            producer = "Тосио Судзуки",
            releaseDate = "2001",
            runningTime = "125",
            rating = "97",
            imageUrl = ""
        ),
        Film(
            id = "2",
            title = "Мой сосед Тоторо",
            originalTitle = "となりのトトロ",
            description = "Две сестры знакомятся с удивительными лесными существами и открывают для себя волшебный мир.",
            director = "Хаяо Миядзаки",
            producer = "Тору Хара",
            releaseDate = "1988",
            runningTime = "86",
            rating = "93",
            imageUrl = ""
        ),
        Film(
            id = "3",
            title = "Ходячий замок",
            originalTitle = "ハウルの動く城",
            description = "Софи оказывается под заклятием и отправляется в путешествие вместе с загадочным волшебником Хаулом.",
            director = "Хаяо Миядзаки",
            producer = "Тосио Судзуки",
            releaseDate = "2004",
            runningTime = "119",
            rating = "87",
            imageUrl = ""
        ),
        Film(
            id = "4",
            title = "Принцесса Мононоке",
            originalTitle = "もののけ姫",
            description = "Принц Аситака оказывается между людьми, стремящимися покорить лес, и его защитниками.",
            director = "Хаяо Миядзаки",
            producer = "Тосио Судзуки",
            releaseDate = "1997",
            runningTime = "134",
            rating = "93",
            imageUrl = ""
        )
    )

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    fun getFilmById(id: String): Film? {
        return films.find { it.id == id }
    }

    fun toggleFavorite(filmId: String) {
        _favoriteIds.value =
            if (filmId in _favoriteIds.value) {
                _favoriteIds.value - filmId
            } else {
                _favoriteIds.value + filmId
            }
    }

    fun getFavoriteFilms(): List<Film> {
        return films.filter { it.id in _favoriteIds.value }
    }
}