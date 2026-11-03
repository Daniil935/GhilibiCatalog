package com.practicum.myapplication.domain.model

import com.google.gson.annotations.SerializedName

data class Film(
    val id: String,
    val title: String,

    @SerializedName("original_title")
    val originalTitle: String,

    val description: String,
    val director: String,
    val producer: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("running_time")
    val runningTime: String,

    @SerializedName("rt_score")
    val rating: String,

    @SerializedName("image")
    val imageUrl: String
)