package com.practicum.myapplication.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.domain.model.UserProfile
import com.practicum.myapplication.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun EditProfileScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onDone: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var fullName by rememberSaveable { mutableStateOf("") }
    var avatarUri by rememberSaveable { mutableStateOf("") }
    var portfolioUrl by rememberSaveable { mutableStateOf("") }
    var favoriteFilm by rememberSaveable { mutableStateOf("") }
    var favoriteDirector by rememberSaveable { mutableStateOf("") }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var cameraUri by remember { mutableStateOf<android.net.Uri?>(null) }

    LaunchedEffect(profile) {
        fullName = profile.fullName
        avatarUri = profile.avatarUri
        portfolioUrl = profile.portfolioUrl
        favoriteFilm = profile.favoriteFilm
        favoriteDirector = profile.favoriteDirector
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            avatarUri = uri.toString()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            avatarUri = cameraUri?.toString().orEmpty()
        }
    }

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Изменить фотографию") },
            text = { Text("Выбери источник фотографии") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPhotoDialog = false
                        galleryLauncher.launch(arrayOf("image/*"))
                    }
                ) {
                    Text("Галерея")
                }
            },
            dismissButton = {
                Column {
                    TextButton(
                        onClick = {
                            showPhotoDialog = false
                            val photoFile = File.createTempFile(
                                "profile_photo_",
                                ".jpg",
                                context.cacheDir
                            )
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                photoFile
                            )
                            cameraUri = uri
                            cameraLauncher.launch(uri)
                        }
                    ) {
                        Text("Камера")
                    }
                    TextButton(onClick = { showPhotoDialog = false }) {
                        Text("Отмена")
                    }
                }
            }
        )
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Редактирование профиля",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.padding(8.dp))

            ProfileAvatar(
                context = context,
                avatarUri = avatarUri,
                modifier = Modifier
                    .padding(8.dp)
                    .size(132.dp)
                    .clickable { showPhotoDialog = true }
            )

            TextButton(onClick = { showPhotoDialog = true }) {
                Text("Изменить фотографию")
            }

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = favoriteFilm,
                onValueChange = { favoriteFilm = it },
                label = { Text("Любимый фильм Studio Ghibli") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = favoriteDirector,
                onValueChange = { favoriteDirector = it },
                label = { Text("Любимый режиссёр") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = portfolioUrl,
                onValueChange = { portfolioUrl = it },
                label = { Text("Ссылка на PDF-портфолио") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    viewModel.saveProfile(
                        UserProfile(
                            fullName = fullName.trim(),
                            avatarUri = avatarUri,
                            portfolioUrl = portfolioUrl.trim(),
                            favoriteFilm = favoriteFilm.trim(),
                            favoriteDirector = favoriteDirector.trim()
                        )
                    )
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад без сохранения")
            }
        }
    }
}
