package com.practicum.myapplication.data

class FilmRepository(
    private val api: GhibliApi = RetrofitClient.api
) {

    suspend fun getFilms(): List<Film> {
        val response = api.getFilms()

        if (!response.isSuccessful) {
            throw Exception("Ошибка сервера: ${response.code()}")
        }

        return response.body()
            ?: throw Exception("Сервер вернул пустой ответ")
    }

    suspend fun getFilmById(id: String): Film {
        val response = api.getFilmById(id)

        if (!response.isSuccessful) {
            throw Exception("Ошибка сервера: ${response.code()}")
        }

        return response.body()
            ?: throw Exception("Фильм не найден")
    }
}