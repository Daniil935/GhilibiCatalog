package com.practicum.myapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.myapplication.data.local.dao.FavoriteFilmDao
import com.practicum.myapplication.data.local.entity.FilmEntity

@Database (
    entities = [FilmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteFilmDao(): FavoriteFilmDao
}