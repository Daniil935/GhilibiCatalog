package com.practicum.myapplication.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.viewmodel.ProfileViewModel
import android.graphics.Matrix

@Composable
fun ProfileScreen(
    viewModelFactory: ViewModelProvider.Factory,
    onEditClick: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Мой профиль",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(24.dp))

            ProfileAvatar(
                context = context,
                avatarUri = profile.avatarUri,
                modifier = Modifier.size(132.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = profile.fullName.ifBlank { "Имя пока не указано" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Любимый фильм",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = profile.favoriteFilm.ifBlank { "Не указан" },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Text(
                        text = "Любимый режиссёр",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = profile.favoriteDirector.ifBlank { "Не указан" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.size(24.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать профиль")
            }

            Button(
                onClick = { viewModel.downloadPortfolio(context) },
                enabled = profile.portfolioUrl.startsWith("https://") ||
                        profile.portfolioUrl.startsWith("http://"),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Портфолио (скачать PDF)")
            }
        }
    }
}

@Composable
internal fun ProfileAvatar(
    context: Context,
    avatarUri: String,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(avatarUri) {
        if (avatarUri.isBlank()) {
            null
        } else {
            runCatching {
                val uri = Uri.parse(avatarUri)

                val originalBitmap = context.contentResolver
                    .openInputStream(uri)
                    .use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }

                if (originalBitmap != null) {
                    val orientation = context.contentResolver
                        .openInputStream(uri)
                        .use { stream ->
                            if (stream != null) {
                                when (
                                    ExifInterface(stream).getAttributeInt(
                                        ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_NORMAL
                                    )
                                ) {
                                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                                    else -> 0
                                }
                            } else {
                                0
                            }
                        }

                    if (orientation != 0) {
                        val matrix = Matrix().apply {
                            postRotate(orientation.toFloat())
                        }

                        Bitmap.createBitmap(
                            originalBitmap,
                            0,
                            0,
                            originalBitmap.width,
                            originalBitmap.height,
                            matrix,
                            true
                        ).asImageBitmap()
                    } else {
                        originalBitmap.asImageBitmap()
                    }
                } else {
                    null
                }
            }.getOrNull()
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = "Фотография профиля",
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Column(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "👤",
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}
