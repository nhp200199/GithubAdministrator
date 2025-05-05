package com.phucnguyen.githubadministrator.core.data

sealed class Result<out T : Any, out E: Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : Result<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<E: Any>(val body: E, val code: Int?) : Result<Nothing, E>()

    data class OperationError(val exception: Exception): Result<Nothing, Nothing>()

    override fun toString(): String {
        return when(this) {
            is ApiError -> "ApiError[code=$code]"
            is Success -> "Success[body=$body]"
            is OperationError -> "OperationError[exception=${exception.message}]"
        }
    }
}
