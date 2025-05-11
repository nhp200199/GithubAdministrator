package com.phucnguyen.githubadministrator.common.data.dataSource.local

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import javax.inject.Inject

/**
 * Local data source for user-related operations.
 *
 * This class provides methods to interact with the local database for user data.
 * It uses a [UserDao] to perform database operations.
 */
class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) : IUserLocalDataSource {

    /**
     * Updates a user in the local database.
     *
     * @param user The [UserEntity] to update.
     * @return A [ResultData] indicating the success or failure of the operation.
     *   [ResultData.Success] if the update was successful, [ResultData.OperationError] otherwise.
     */
    override suspend fun updateUser(user: UserEntity): ResultData<Unit> {
        return try {
            userDao.updateUser(user)
            ResultData.Success(Unit)
        } catch (e: Exception) {
            ResultData.OperationError(e)
        }
    }

    /**
     * Retrieves a user from the local database by their username.
     *
     * @param userName The username of the user to retrieve.
     * @return A [ResultData] containing the [UserDetail] if the user is found and their
     *   details have been fetched, [ResultData.ApiError] if the user is found but details
     *   are not fetched, or [ResultData.OperationError] if a database error occurs.
     */
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