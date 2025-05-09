package com.phucnguyen.githubadministrator.core.data

sealed class ResultData<out T : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val data: T) : ResultData<T>()

    /**
     * Failure response with body
     */
    data class ApiError(val message: String, val code: Int? = null) : ResultData<Nothing>()

    data class OperationError(val exception: Exception): ResultData<Nothing>()

    override fun toString(): String {
        return when(this) {
            is ApiError -> "ApiError[code=$code]"
            is Success -> "Success[body=$data]"
            is OperationError -> "OperationError[exception=${exception.message}]"
        }
    }
}
