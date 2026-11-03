package com.practicum.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.viewmodel.FilmViewModel

@Composable
fun FavoritesScreen(
    onFilmClick: (String) -> Unit,
    viewModel: FilmViewModel = viewModel()
) {
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val films by viewModel.films.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val favorites = films.filter { it.id in favoriteIds }
    Scaffold(
        topBar = {
            Text(
                text = "Избранное",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        if (favorites.isEmpty() && isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()

                Text(
                    text = "Загружаем фильмы...",
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        } else if (favorites.isEmpty() && error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Не удалось загрузить фильмы",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = error ?: "Неизвестная ошибка",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Button(
                    onClick = { viewModel.loadFilms() },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Повторить")
                }
            }
        } else if (favorites.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Пока здесь пусто",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Добавляй понравившиеся фильмы в избранное."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites, key = { it.id }) { film ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFilmClick(film.id) }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = film.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(text = "Год выпуска: ${film.releaseDate}")
                        }
                    }
                }
            }
        }
    }
}