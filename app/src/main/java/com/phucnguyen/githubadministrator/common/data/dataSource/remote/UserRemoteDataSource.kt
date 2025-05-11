package com.phucnguyen.githubadministrator.common.data.dataSource.remote

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.common.utils.safeApiCall
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import javax.inject.Inject

/**
 * Remote data source for fetching user data from the GitHub API.
 *
 * This class is responsible for making network calls to retrieve user information
 * using the provided [IUserService]. It utilizes the `safeApiCall` utility
 * to handle potential network errors and exceptions.
 */
class UserRemoteDataSource @Inject constructor(
    private val userService: IUserService
) : IUserRemoteDataSource {

    /**
     * Fetches a list of user overviews from the GitHub API.
     *
     * @param since The ID of the last user from the previous page (for pagination).
     * @param perPage The number of users to retrieve per page.
     * @return A [ResultData] containing a [NetworkResponse] with a list of [UserOverview]
     *   if the API call is successful, or an error ([ResultData.ApiError] or
     *   [ResultData.OperationError]) otherwise.
     */
    override suspend fun getUsers(
        since: Int,
        perPage: Int
    ): ResultData<NetworkResponse<List<UserOverview>>> {
        val result = safeApiCall { userService.getUsers(since, perPage) }

        return handleNetworkResult(
            result = result,
            mapper = { users -> users.map { it.toUserOverview() } }
        )
    }

    /**
     * Fetches the detailed information for a specific user from the GitHub API.
     *
     * @param userName The username of the user to retrieve details for.
     * @return A [ResultData] containing a [NetworkResponse] with a [UserDetail]
     *   if the API call is successful, or an error ([ResultData.ApiError] or
     *   [ResultData.OperationError]) otherwise.
     */
    override suspend fun getUserDetail(userName: String): ResultData<NetworkResponse<UserDetail>> {
        val result = safeApiCall { userService.getUserDetail(userName) }

        return handleNetworkResult(
            result = result,
            mapper = { it.toUserDetail() }
        )
    }

    /**
     * Handles the result of a network call and maps the response body.
     *
     * This private helper function takes a [ResultData] containing a [NetworkResponse]
     * with a raw response body of type [T] and a mapper function to transform it
     * into a desired type [R]. It preserves the header information from the original
     * network response.
     *
     * @param result The result of the network call.
     * @param mapper A function to transform the response body of type [T] to type [R].
     * @return A [ResultData] containing a [NetworkResponse] with the mapped body of type [R],
     *   or the original error result if the network call was not successful.
     */
    private fun <T: Any, R: Any> handleNetworkResult(
        result: ResultData<NetworkResponse<T>>,
        mapper: (T) -> R
    ): ResultData<NetworkResponse<R>> {
        return when (result) {
            is ResultData.Success -> ResultData.Success(
                NetworkResponse(
                    result.data.header,
                    mapper(result.data.body)
                )
            )
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}