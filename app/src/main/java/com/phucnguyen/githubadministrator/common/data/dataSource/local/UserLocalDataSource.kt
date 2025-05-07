package com.phucnguyen.githubadministrator.common.data.dataSource.local

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) : IUserLocalDataSource {
    override suspend fun insertUser(user: UserEntity): ResultData<Unit> {
        return try {
            userDao.insertUser(user)
            ResultData.Success(Unit)
        } catch (e: Exception) {
            ResultData.OperationError(e)
        }
    }

    override suspend fun getUserByName(userName: String): ResultData<UserDetail> {
        return try {
            val user = userDao.getUserByName(userName)
            ResultData.Success(user.toUserDetail())
        } catch (e: Exception) {
            ResultData.OperationError(e)
        }
    }
}