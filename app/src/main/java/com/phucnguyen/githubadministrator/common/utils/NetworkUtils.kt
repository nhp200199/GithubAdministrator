package com.phucnguyen.githubadministrator.common.utils

import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.exception.UnknownException
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import retrofit2.Response
import java.io.IOException

/**
 * Extracts the 'since' parameter from a GitHub API Link header for pagination.
 *
 * The Link header is used by the GitHub API to provide links to the next, previous,
 * first, and last pages of results. This function specifically looks for the link
 * with `rel="next"` and extracts the value of the `since` query parameter from its URL.
 *
 * @param linkHeader The value of the 'Link' header from the API response. Can be null.
 * @return The integer value of the 'since' parameter for the next page, or null if
 *   the Link header is null, the "next" link is not found, or the "since" parameter
 *   is not present in the "next" link.
 */
fun extractNextSinceParameter(linkHeader: String?): Int? {
    if (linkHeader == null) return null

    val links = linkHeader.split(",")

    for (link in links) {
        val parts = link.split(";")
        if (parts.size >= 2) {
            val urlPart = parts[0].trim()
            val relPart = parts[1].trim()

            if (relPart.contains("""rel="next"""")) {
                val url = urlPart.substringAfter("<").substringBefore(">")

                val sinceMatch = Regex("""since=(\d+)""").find(url)
                return sinceMatch?.groupValues?.get(1)?.toIntOrNull()
            }
        }
    }
    return null
}

/**
 * Safely executes a Retrofit API call and wraps the result in a [ResultData].
 *
 * This function handles potential exceptions during the API call and checks the
 * response for success or failure. It returns a [ResultData.Success] with a
 * [NetworkResponse] containing the response headers and body if the call is
 * successful and has a body. If the response is successful but has no body,
 * it returns a [ResultData.ApiError] indicating an empty response body. If the
 * response is not successful, it returns a [ResultData.ApiError] with the error
 * message and code. If an exception occurs during the API call (e.g., network
 * issues), it returns a [ResultData.OperationError] with a specific exception
 * type.
 *
 * @param apiCall A suspend function that performs the Retrofit API call and returns a [Response].
 * @return A [ResultData] representing the outcome of the API call.
 */
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