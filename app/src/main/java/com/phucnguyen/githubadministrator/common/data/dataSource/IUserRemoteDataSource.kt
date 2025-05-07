package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse

interface IUserRemoteDataSource {
    suspend fun getUsers(
        since: Int = 0,
        perPage: Int = 20
    ): ResultData<NetworkResponse<List<UserOverview>>>

    suspend fun getUserDetail(userName: String): ResultData<NetworkResponse<UserDetail>>
}