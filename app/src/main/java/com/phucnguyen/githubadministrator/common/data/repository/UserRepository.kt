package com.phucnguyen.githubadministrator.common.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.phucnguyen.githubadministrator.common.data.dataSource.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.data.paging.UserPagingSource
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
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

    override suspend fun getUserDetail(userName: String): ResultData<UserDetail> {
        val result = userRemoteDataSource.getUserDetail(userName)

        return when (result) {
            is ResultData.Success -> ResultData.Success(result.data.body)
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}