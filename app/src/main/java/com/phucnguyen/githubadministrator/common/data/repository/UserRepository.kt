package com.phucnguyen.githubadministrator.common.data.repository

import com.phucnguyen.githubadministrator.common.data.dataSource.local.IUserLocalDataSource
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: IUserLocalDataSource,
    private val userRemoteDataSource: IUserRemoteDataSource,
) : IUserRepository {
    override suspend fun getUserDetail(userName: String): ResultData<UserDetail> {
        val localUserResult = userLocalDataSource.getUserByName(userName)
        if (localUserResult is ResultData.Success) {
            return ResultData.Success(localUserResult.data)
        }

        val result = userRemoteDataSource.getUserDetail(userName)

        return when (result) {
            is ResultData.Success -> {
                userLocalDataSource.updateUser(
                    result.data.body.toUserEntity().copy(detailFetched = true)
                )
                ResultData.Success(result.data.body)
            }
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}