package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse

interface IUserRemoteDataSource {
    suspend fun getUsers(
        since: Long = 0,
        perPage: Int = 20
    ): Result<List<UserOverview>, ErrorResponse>

    suspend fun getUserDetail(userName: String): Result<UserDetail, ErrorResponse>
}