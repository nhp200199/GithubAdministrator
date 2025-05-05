package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService

class UserRemoteDataSource(
    private val userService: IUserService
) : IUserRemoteDataSource {
    override suspend fun getUsers(
        since: Long,
        perPage: Int
    ): Result<List<UserOverview>, ErrorResponse> {
        val result = userService.getUsers(since, perPage)

        //TODO: handle 304 code
        return when (result) {
            is Result.Success -> Result.Success(result.body.map { it.toUserOverview() })
            is Result.ApiError -> result
            is Result.OperationError -> result
        }
    }

    override suspend fun getUserDetail(userName: String): Result<UserDetail, ErrorResponse> {
        val result = userService.getUserDetail(userName)

        return when (result) {
            is Result.Success -> Result.Success(result.body.toUserDetail())
            is Result.ApiError -> result
            is Result.OperationError -> result
        }
    }
}