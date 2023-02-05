package com.mikelike11.kinopoiskapp.screens.filmdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mikelike11.kinopoiskapp.data.local.FilmDatabase
import com.mikelike11.kinopoiskapp.data.model.DetailedFilm
import com.mikelike11.kinopoiskapp.data.repository.FilmRepository
import com.mikelike11.kinopoiskapp.data.repository.FilmRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FilmDetailsViewModel constructor(
    application: Application,
    private val filmRepository: FilmRepository = FilmRepositoryImpl(
        FilmDatabase.getDatabase(application).filmDao()
    )
) : AndroidViewModel(application) {
    val film: MutableLiveData<DetailedFilm> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    fun getFilmFullInfo(id: Int) {
        compositeDisposable.addAll(
            filmRepository.getFilmFullInfo(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { t1, t2 ->
                    film.value = t1
                }
        )

    }
}