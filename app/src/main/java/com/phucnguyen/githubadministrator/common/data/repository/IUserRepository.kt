package com.phucnguyen.githubadministrator.common.data.repository

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData

interface IUserRepository {
    suspend fun getUserDetail(userName: String): ResultData<UserDetail>
}