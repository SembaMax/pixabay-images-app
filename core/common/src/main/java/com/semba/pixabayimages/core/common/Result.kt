package com.semba.pixabayimages.core.common

sealed class Result<T>(
    val data: T? = null,
    val errorCode: Int? = null
) {
    class Success<T>(data: T) : Result<T>(data = data, errorCode = null)
    class Failure<T>(errorCode: Int) : Result<T>(data = null, errorCode = errorCode)
}