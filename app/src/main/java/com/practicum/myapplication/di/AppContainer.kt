package com.practicum.myapplication.di

import com.practicum.myapplication.data.repository.FilmRepositoryImpl
import com.practicum.myapplication.domain.repository.FilmRepository
import com.practicum.myapplication.domain.usecase.GetFilmByIdUseCase
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase

class AppContainer {

    val filmRepository: FilmRepository by lazy {
        FilmRepositoryImpl()
    }

    val getFilmsUseCase: GetFilmsUseCase by lazy {
        GetFilmsUseCase(filmRepository)
    }

    val getFilmByIdUseCase: GetFilmByIdUseCase by lazy {
        GetFilmByIdUseCase(filmRepository)
    }
    val viewModelFactory: AppViewModelFactory by lazy {
        AppViewModelFactory(this)
    }
}