package com.phucnguyen.githubadministrator.common.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.phucnguyen.githubadministrator.common.data.dataSource.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.data.paging.UserPagingSource
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: IUserRemoteDataSource,
    private val userPagingSource: UserPagingSource,
) : IUserRepository {
    override fun getPagingUsers(): Flow<PagingData<UserOverview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { userPagingSource }
        ).flow
    }

    override suspend fun getUserDetail(userName: String): Result<UserDetail, ErrorResponse> {
        val result = userRemoteDataSource.getUserDetail(userName)

        return when (result) {
            is Result.Success -> Result.Success(result.data.body)
            is Result.ApiError -> result
            is Result.OperationError -> result
        }
    }
}