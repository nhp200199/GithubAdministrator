package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse

interface IUserRemoteDataSource {
    suspend fun getUsers(
        since: Int = 0,
        perPage: Int = 20
    ): Result<NetworkResponse<List<UserOverview>>, ErrorResponse>

    suspend fun getUserDetail(userName: String): Result<NetworkResponse<UserDetail>, ErrorResponse>
}