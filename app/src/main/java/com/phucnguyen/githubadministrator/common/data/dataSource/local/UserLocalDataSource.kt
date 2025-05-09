package com.phucnguyen.githubadministrator.common.data.dataSource.local

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) : IUserLocalDataSource {
    override suspend fun updateUser(user: UserEntity): ResultData<Unit> {
        return try {
            userDao.updateUser(user)
            ResultData.Success(Unit)
        } catch (e: Exception) {
            ResultData.OperationError(e)
        }
    }

    override suspend fun getUserByName(userName: String): ResultData<UserDetail> {
        return try {
            val user = userDao.getUserByName(userName)
            if (user.detailFetched) {
                ResultData.Success(user.toUserDetail())
            } else {
                ResultData.ApiError("User detail not fetched")
            }
        } catch (e: Exception) {
            ResultData.OperationError(e)
        }
    }
}