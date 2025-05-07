package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse

interface IUserRemoteDataSource {
    suspend fun getUsers(
        since: Int = 0,
        perPage: Int = 20
    ): ResultData<List<UserOverview>>

    suspend fun getUserDetail(userName: String): ResultData<UserDetail>
}