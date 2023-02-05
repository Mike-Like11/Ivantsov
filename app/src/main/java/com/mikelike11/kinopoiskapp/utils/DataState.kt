package com.mikelike11.kinopoiskapp.utils

sealed class DataState<T> {
    class Loading<T> : DataState<T>()
    class Success<T>(var data: T) : DataState<T>()
    class Error<T>(val error: Throwable) : DataState<T>()
}