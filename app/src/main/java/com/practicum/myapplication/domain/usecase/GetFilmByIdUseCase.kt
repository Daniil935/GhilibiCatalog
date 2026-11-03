package com.practicum.myapplication.domain.usecase

import com.practicum.myapplication.domain.repository.FilmRepository

class GetFilmByIdUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(id: String) = repository.getFilmById(id)
}