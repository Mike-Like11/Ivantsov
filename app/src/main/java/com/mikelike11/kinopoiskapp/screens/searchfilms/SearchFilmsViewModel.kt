package com.mikelike11.kinopoiskapp.screens.searchfilms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikelike11.kinopoiskapp.data.local.FilmDatabase
import com.mikelike11.kinopoiskapp.data.model.Film
import com.mikelike11.kinopoiskapp.data.repository.FilmRepository
import com.mikelike11.kinopoiskapp.data.repository.FilmRepositoryImpl
import com.mikelike11.kinopoiskapp.screens.favoritefilms.FavoriteFilmsViewModel
import com.mikelike11.kinopoiskapp.screens.filmdetails.FilmDetailsViewModel
import com.mikelike11.kinopoiskapp.utils.DataState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ViewModelFactory(
    private val app: Application,
) : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(
                SearchFilmsViewModel::class.java
            )
        ) {

            return SearchFilmsViewModel(
                app,
                FilmRepositoryImpl(FilmDatabase.getDatabase(app).filmDao())
            ) as T
        } else {
            if (modelClass.isAssignableFrom(
                    FilmDetailsViewModel::class.java
                )
            ) {

                return FilmDetailsViewModel(
                    app,
                    FilmRepositoryImpl(FilmDatabase.getDatabase(app).filmDao())
                ) as T
            } else {
                if (modelClass.isAssignableFrom(
                        FavoriteFilmsViewModel::class.java
                    )
                ) {

                    return FavoriteFilmsViewModel(
                        app,
                        FilmRepositoryImpl(FilmDatabase.getDatabase(app).filmDao())
                    ) as T
                }
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SearchFilmsViewModel constructor(
    application: Application,
    private val filmRepository: FilmRepository = FilmRepositoryImpl(
        FilmDatabase.getDatabase(application).filmDao()
    )
) : AndroidViewModel(application) {
    val topFilms: MutableLiveData<DataState<ArrayList<Film>>> = MutableLiveData()
    private var fetchedPopularFilms: ArrayList<Film> = ArrayList()
    val compositeDisposable = CompositeDisposable()
    fun getTopFilms() {
        topFilms.value = DataState.Loading()
        var favoriteFilms: List<Film> = mutableListOf()
        compositeDisposable.addAll(
            filmRepository.getFavoriteFilms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    favoriteFilms = it
                }
        )
        compositeDisposable.addAll(
            filmRepository.getTopFilms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError { topFilms.value = DataState.Error(error = it) }
                .subscribe({
                    it.forEach { film ->
                        if (favoriteFilms.filter { it.filmId == film.filmId }.size > 0) {
                            film.isInFavorites = true
                        }
                    }
                    fetchedPopularFilms = it
                    topFilms.value = DataState.Success(it)
                },
                    {
                        topFilms.value = DataState.Error(error = it)
                    })
        )
    }

    fun addtoFavorite(film: Film) {
        if (film.isInFavorites)
            filmRepository.addToFavoriteFilms(film)
        else
            filmRepository.deleteFromFavoriteFilms(film)

    }

    fun getFavoriteFilmsByName(name: String) {
        if (name.isEmpty()) {
            topFilms.value = DataState.Success(fetchedPopularFilms)
        } else {
            fetchedPopularFilms.filter {
                it.nameRu.lowercase()
                    .contains(
                        name.lowercase()
                    )
            }
            topFilms.value = DataState.Success(ArrayList(fetchedPopularFilms.filter {
                it.nameRu.lowercase()
                    .contains(
                        name.lowercase()
                    )
            }))
        }
    }
}