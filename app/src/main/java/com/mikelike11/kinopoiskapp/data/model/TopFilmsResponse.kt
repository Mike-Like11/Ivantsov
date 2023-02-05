package com.mikelike11.kinopoiskapp.data.model


data class TopFilmsResponse(
    val pagesCount: Int,
    var films: ArrayList<Film> = arrayListOf()
)