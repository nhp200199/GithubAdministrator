package com.phucnguyen.githubadministrator.common.data.dataSource.remote

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.common.utils.safeApiCall
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: IUserService
) : IUserRemoteDataSource {
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

    override suspend fun getUserDetail(userName: String): ResultData<NetworkResponse<UserDetail>> {
        val result = safeApiCall { userService.getUserDetail(userName) }

        return handleNetworkResult(
            result = result,
            mapper = { it.toUserDetail() }
        )
    }

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