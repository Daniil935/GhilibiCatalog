package com.practicum.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.myapplication.viewmodel.FilterSettingsViewModel
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
@Composable
fun FilterSettingsScreen(
    onDone: () -> Unit,
    onBackClick: () -> Unit,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: FilterSettingsViewModel = viewModel(
        factory = viewModelFactory
    )

    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад"
                )
            }

            Text(text = "Настройки фильтрации")
        }

        OutlinedTextField(
            value = settings.title,
            onValueChange = viewModel::updateTitle,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Название фильма") },
            singleLine = true
        )

        OutlinedTextField(
            value = settings.director,
            onValueChange = viewModel::updateDirector,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Режиссёр") },
            singleLine = true
        )

        OutlinedTextField(
            value = settings.releaseYear,
            onValueChange = viewModel::updateReleaseYear,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Год выпуска") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = {
                viewModel.saveSettings(onDone)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Готово")
        }

        Button(
            onClick = viewModel::clearSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сбросить фильтры")
        }
    }
}