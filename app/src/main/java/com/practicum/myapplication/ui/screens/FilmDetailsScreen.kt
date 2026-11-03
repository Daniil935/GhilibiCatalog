package com.practicum.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.domain.model.Film
import com.practicum.myapplication.viewmodel.FilmDetailsViewModel

@Composable
fun FilmDetailsScreen(
    filmId: String,
    onBackClick: () -> Unit,
    viewModel: FilmDetailsViewModel = viewModel()
) {
    val film by viewModel.film.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    LaunchedEffect(filmId) {
        viewModel.loadFilm(filmId)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text("← Назад")
            }

            when {
                isLoading && film == null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Загружаем информацию о фильме...")
                    }
                }

                error != null && film == null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Не удалось загрузить фильм",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(text = error ?: "Произошла ошибка")

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.loadFilm(filmId) }
                        ) {
                            Text("Повторить")
                        }
                    }
                }

                film != null -> {
                    val currentFilm = film!!

                    FilmDetailsContent(
                        film = currentFilm,
                        isFavorite = currentFilm.id in favoriteIds,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun FilmDetailsContent(
    film: Film,
    isFavorite: Boolean,
    viewModel: FilmDetailsViewModel
) {
    Column(


        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        Text(
            text = film.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = film.originalTitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { viewModel.toggleFavorite(film.id) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isFavorite) {
                    "Удалить из избранного"
                } else {
                    "Добавить в избранное"
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Описание",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = film.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Информация о фильме",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(viewModel.getReleaseDateText(film))

        Spacer(Modifier.height(8.dp))

        Text(viewModel.getRunningTimeText(film))

        Spacer(Modifier.height(8.dp))

        Text(viewModel.getRatingText(film))

        Spacer(Modifier.height(8.dp))

        Text("Режиссёр: ${film.director}")

        Spacer(Modifier.height(8.dp))

        Text("Продюсер: ${film.producer}")
    }
}