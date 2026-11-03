package com.practicum.myapplication.data

import com.practicum.myapplication.domain.model.Film
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GhibliApi {

    @GET("films")
    suspend fun getFilms(
        @Query("limit") limit: Int = 50
    ): Response<List<Film>>

    @GET("films/{id}")
    suspend fun getFilmById(
        @Path("id") id: String
    ): Response<Film>
}