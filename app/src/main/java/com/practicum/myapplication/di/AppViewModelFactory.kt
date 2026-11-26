package com.practicum.myapplication.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.myapplication.viewmodel.FavoritesViewModel
import com.practicum.myapplication.viewmodel.FilmDetailsViewModel
import com.practicum.myapplication.viewmodel.FilmListViewModel

class AppViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel = when {
            modelClass.isAssignableFrom(FilmListViewModel::class.java) -> {
                FilmListViewModel(appContainer.getFilmsUseCase)
            }

            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(appContainer.getFilmsUseCase)
            }

            modelClass.isAssignableFrom(FilmDetailsViewModel::class.java) -> {
                FilmDetailsViewModel(appContainer.getFilmByIdUseCase)
            }

            else -> {
                throw IllegalArgumentException(
                    "Неизвестная ViewModel: ${modelClass.name}"
                )
            }
        }

        return viewModel as T
    }
}