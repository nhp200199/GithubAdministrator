package com.phucnguyen.githubadministrator.common.data.dataSource.local

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

interface IUserLocalDataSource {
    suspend fun insertUser(user: UserEntity): ResultData<Unit>
    suspend fun getUserByName(userName: String): ResultData<UserDetail>
}