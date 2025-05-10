package com.phucnguyen.githubadministrator.common.utils

import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.exception.UnknownException
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import retrofit2.Response
import java.io.IOException

fun extractNextSinceParameter(linkHeader: String?): Int? {
    if (linkHeader == null) return null

    val links = linkHeader.split(",")

    for (link in links) {
        val parts = link.split(";")
        if (parts.size >= 2) {
            val urlPart = parts[0].trim()
            val relPart = parts[1].trim()

            // Check if the relation is "next"
            if (relPart.contains("""rel="next"""")) {
                // Extract the URL from the angle brackets
                val url = urlPart.substringAfter("<").substringBefore(">")

                // Extract the 'since' parameter from the URL
                val sinceMatch = Regex("""since=(\d+)""").find(url)
                return sinceMatch?.groupValues?.get(1)?.toIntOrNull()
            }
        }
    }
    // Return null if the "next" link or "since" parameter is not found
    return null
}

suspend fun <T: Any> safeApiCall(apiCall: suspend () -> Response<T>): ResultData<NetworkResponse<T>> {
    return try {
        val response = apiCall()
        when {
            response.isSuccessful -> {
                response.body()?.let { ResultData.Success(NetworkResponse(response.headers(), it)) }
                    ?: ResultData.ApiError("Empty response body", response.code())
            }
            else -> ResultData.ApiError(response.errorBody()?.string() ?: "Unknown error", response.code())
        }
    } catch (exception: Throwable) {
        handleApiError(exception)
    }
}

private fun <T: Any> handleApiError(exception: Throwable): ResultData<T> {
    return when (exception) {
        is IOException -> ResultData.OperationError(NoNetworkConnectionException())
        else -> ResultData.OperationError(UnknownException())
    }
}