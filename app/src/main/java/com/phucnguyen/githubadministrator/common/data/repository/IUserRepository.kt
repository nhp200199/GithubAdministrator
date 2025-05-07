package com.phucnguyen.githubadministrator.common.data.repository

import androidx.paging.PagingData
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getPagingUsers(): Flow<PagingData<UserOverview>>
    suspend fun getUserDetail(userName: String): ResultData<UserDetail>
}