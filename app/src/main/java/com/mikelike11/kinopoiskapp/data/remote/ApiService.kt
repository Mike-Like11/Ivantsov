package com.mikelike11.kinopoiskapp.data.remote

import com.mikelike11.kinopoiskapp.data.model.DetailedFilm
import com.mikelike11.kinopoiskapp.data.model.TopFilmsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/v2.2/films/top")
    fun getTopFilms(@Query("type") type: String = "TOP_100_POPULAR_FILMS"): Single<TopFilmsResponse>

    @GET("api/v2.2/films/{id}")
    fun getFilmInfoById(@Path("id") filmId: Int): Single<DetailedFilm>

}