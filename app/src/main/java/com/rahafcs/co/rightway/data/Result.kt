package com.rahafcs.co.rightway.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val e: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> {
                "Success[data: $data"
            }
            is Failure -> {
                "Failure[exception: $e"
            }
        }
    }
}
