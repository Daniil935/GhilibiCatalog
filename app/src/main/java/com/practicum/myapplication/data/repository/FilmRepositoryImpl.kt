package com.practicum.myapplication.data.repository

import com.practicum.myapplication.data.GhibliApi
import com.practicum.myapplication.data.RetrofitClient
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.domain.repository.FilmRepository

class FilmRepositoryImpl(
    private val api: GhibliApi = RetrofitClient.api
) : FilmRepository {

    override suspend fun getFilms(): List<Film> {
        val response = api.getFilms()

        if (!response.isSuccessful) {
            throw Exception("Ошибка сервера: ${response.code()}")
        }

        return response.body()
            ?: throw Exception("Сервер вернул пустой ответ")
    }

    override suspend fun getFilmById(id: String): Film {
        val response = api.getFilmById(id)

        if (!response.isSuccessful) {
            throw Exception("Ошибка сервера: ${response.code()}")
        }

        return response.body()
            ?: throw Exception("Фильм не найден")
    }
}