package com.mikelike11.kinopoiskapp.data.repository

import com.mikelike11.kinopoiskapp.data.model.DetailedFilm
import com.mikelike11.kinopoiskapp.data.model.Film
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FilmRepository {
    fun getTopFilms(): Observable<ArrayList<Film>>
    fun getFilmFullInfo(filmId: Int): Single<DetailedFilm>
    fun getFavoriteFilms(): Observable<List<Film>>
    fun addToFavoriteFilms(film: Film)
    fun deleteFromFavoriteFilms(film: Film)
}