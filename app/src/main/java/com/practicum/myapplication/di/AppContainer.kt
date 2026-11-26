package com.practicum.myapplication.di

import android.content.Context
import androidx.room.Room
import com.practicum.myapplication.data.cache.AppCache
import com.practicum.myapplication.data.local.database.AppDatabase
import com.practicum.myapplication.data.local.preferences.FilterSettingsDataStore
import com.practicum.myapplication.data.repository.FavoriteFilmRepositoryImpl
import com.practicum.myapplication.data.repository.FilmRepositoryImpl
import com.practicum.myapplication.domain.repository.FavoriteFilmRepository
import com.practicum.myapplication.domain.repository.FilmRepository
import com.practicum.myapplication.domain.usecase.GetFilmByIdUseCase
import com.practicum.myapplication.domain.usecase.GetFilmsUseCase

class AppContainer(context: Context) {

    // Локальная база данных Room
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "ghibli_database"
        ).build()
    }

    // Репозиторий избранных фильмов
    val favoriteFilmRepository: FavoriteFilmRepository by lazy {
        FavoriteFilmRepositoryImpl(database.favoriteFilmDao())
    }

    // Сетевой репозиторий фильмов
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
    val filterSettingsDataStore: FilterSettingsDataStore by lazy {
        FilterSettingsDataStore(context.applicationContext)
    }
    val appCache: AppCache by lazy {
        AppCache()
    }
}