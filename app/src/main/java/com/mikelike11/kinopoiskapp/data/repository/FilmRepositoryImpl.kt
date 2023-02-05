package com.mikelike11.kinopoiskapp.data.repository

import com.mikelike11.kinopoiskapp.data.local.FilmDao
import com.mikelike11.kinopoiskapp.data.model.DetailedFilm
import com.mikelike11.kinopoiskapp.data.model.Film
import com.mikelike11.kinopoiskapp.data.remote.RetrofitServiceBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class FilmRepositoryImpl constructor(private val filmDao: FilmDao) : FilmRepository {
    override fun getTopFilms(): Observable<ArrayList<Film>> {
        return RetrofitServiceBuilder.getService().getTopFilms().toObservable().map { it.films }
    }

    override fun getFilmFullInfo(filmId: Int): Single<DetailedFilm> {
        return RetrofitServiceBuilder.getService().getFilmInfoById(filmId)
    }

    override fun getFavoriteFilms(): Observable<List<Film>> {
        return filmDao.getAll()
    }

    override fun addToFavoriteFilms(film: Film) {
        filmDao.insert(film).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun deleteFromFavoriteFilms(film: Film) {
        Observable.fromAction<Void> {
            filmDao.delete(film)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

}