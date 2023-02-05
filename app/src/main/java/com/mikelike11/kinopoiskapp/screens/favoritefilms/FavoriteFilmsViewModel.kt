package com.mikelike11.kinopoiskapp.screens.favoritefilms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mikelike11.kinopoiskapp.data.local.FilmDatabase
import com.mikelike11.kinopoiskapp.data.model.Film
import com.mikelike11.kinopoiskapp.data.repository.FilmRepository
import com.mikelike11.kinopoiskapp.data.repository.FilmRepositoryImpl
import com.mikelike11.kinopoiskapp.utils.DataState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoriteFilmsViewModel constructor(application: Application, private val filmRepository: FilmRepository = FilmRepositoryImpl(
    FilmDatabase.getDatabase(application).filmDao())) : AndroidViewModel(application) {
    private var fetchedFavoriteFilms: ArrayList<Film> = ArrayList()
    val favoriteFilms: MutableLiveData<DataState<ArrayList<Film>>> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    fun getFavoriteFilms() {
        favoriteFilms.value = DataState.Loading()
        compositeDisposable.addAll(
            filmRepository.getFavoriteFilms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError { favoriteFilms.value = DataState.Error(error = it) }
                .subscribe {
                    it.forEach {
                        it.isInFavorites = true
                    }
                    favoriteFilms.value = DataState.Success(ArrayList(it))
                    fetchedFavoriteFilms = ArrayList(it)
                }
        )
    }
    fun deleteFavoriteFilm(film:Film){
        filmRepository.deleteFromFavoriteFilms(film)
    }
    fun getFavoriteFilmsByName(name:String){
        if (name.isEmpty()){
            favoriteFilms.value = DataState.Success(fetchedFavoriteFilms)
        }
        else{
            fetchedFavoriteFilms.filter { it.nameRu.lowercase()
                .contains(
                    name.lowercase()
                ) }
            favoriteFilms.value = DataState.Success(ArrayList(fetchedFavoriteFilms.filter { it.nameRu.lowercase()
                .contains(
                    name.lowercase()
                ) }))
        }
    }
}