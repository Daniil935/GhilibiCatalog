package com.practicum.myapplication.domain.usecase

import com.practicum.myapplication.domain.repository.FilmRepository

class GetFilmsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke() = repository.getFilms()
}