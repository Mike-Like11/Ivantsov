package com.mikelike11.kinopoiskapp.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films")
data class Film(
    @PrimaryKey
    val filmId: Int,
    val nameRu: String,
    val nameEn: String?,
    val year: String?,
    val filmLength: String?,
    val countries: ArrayList<Country> = arrayListOf(),
    val genres: ArrayList<Genre> = arrayListOf(),
    val rating: String?,
    val ratingVoteCount: Int?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val ratingChange: String?,
) {
    @Ignore
    var isInFavorites: Boolean = false
}