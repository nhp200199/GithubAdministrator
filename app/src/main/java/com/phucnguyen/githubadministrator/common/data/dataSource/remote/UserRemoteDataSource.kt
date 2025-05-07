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

        //TODO: handle 304 code
        return when (result) {
            is ResultData.Success -> ResultData.Success(
                NetworkResponse(
                    result.data.header,
                    result.data.body.map { it.toUserOverview() }
                )
            )
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }

    override suspend fun getUserDetail(userName: String): ResultData<NetworkResponse<UserDetail>> {
        val result = safeApiCall { userService.getUserDetail(userName) }

        return when (result) {
            is ResultData.Success -> ResultData.Success(
                NetworkResponse(
                    result.data.header,
                    result.data.body.toUserDetail()
                )
            )
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}