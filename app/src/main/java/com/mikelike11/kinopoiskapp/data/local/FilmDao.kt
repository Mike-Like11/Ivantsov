package com.mikelike11.kinopoiskapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mikelike11.kinopoiskapp.data.model.Film
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface FilmDao {
    @Query("SELECT * FROM favorite_films")
    fun getAll(): Observable<List<Film>>

    @Insert
    fun insert(film: Film): Single<Long>

    @Query("SELECT * FROM favorite_films where filmId=:filmId")
    fun getById(filmId: Int): Film?

    @Delete
    fun delete(film: Film)
}