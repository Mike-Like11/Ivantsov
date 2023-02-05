package com.mikelike11.kinopoiskapp.data.local

import androidx.room.TypeConverter
import com.mikelike11.kinopoiskapp.data.model.Country
import com.mikelike11.kinopoiskapp.data.model.Genre

class FilmConverter {
    @TypeConverter
    fun storingCountriesToString(countries: ArrayList<Country>): String {
        return countries.map { it.country }.joinToString(", ")
    }

    @TypeConverter
    fun storingStringtoCountries(countries: String): ArrayList<Country> {
        return ArrayList(countries.split(", ").map { Country(it) })
    }

    @TypeConverter
    fun storingGenresToString(genres: ArrayList<Genre>): String {
        return genres.map { it.genre }.joinToString(", ")
    }

    @TypeConverter
    fun storingStringtoGenres(countries: String): ArrayList<Genre> {
        return ArrayList(countries.split(", ").map { Genre(it) })
    }
}