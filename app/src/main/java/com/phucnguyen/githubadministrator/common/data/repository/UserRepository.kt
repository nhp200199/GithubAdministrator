package com.phucnguyen.githubadministrator.common.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.phucnguyen.githubadministrator.common.data.dataSource.local.IUserLocalDataSource
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.data.paging.UserPagingSource
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: IUserLocalDataSource,
    private val userRemoteDataSource: IUserRemoteDataSource,
    private val userPagingSource: UserPagingSource,
    private val userDao: UserDao
) : IUserRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingUsers(): Flow<PagingData<UserOverview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = userPagingSource
//            pagingSourceFactory = { userPagingSource }
        ) {
            userDao.getAllUsers()
        }.flow.map {
            it.map { userEntity -> userEntity.toUserOverview() }
        }
    }

    override suspend fun getUserDetail(userName: String): ResultData<UserDetail> {
        val localUserResult = userLocalDataSource.getUserByName(userName)
        if (localUserResult is ResultData.Success) {
            return ResultData.Success(localUserResult.data)
        }

        val result = userRemoteDataSource.getUserDetail(userName)

        return when (result) {
            is ResultData.Success -> {
                userLocalDataSource.insertUser(
                    result.data.body.toUserEntity().copy(detailFetched = true)
                )
                ResultData.Success(result.data.body)
            }
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}