package com.phucnguyen.githubadministrator.common.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.exception.ApiRequestException
import com.phucnguyen.githubadministrator.common.utils.extractNextSinceParameter
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import com.phucnguyen.githubadministrator.core.data.local.room.GithubAdminDatabase
import javax.inject.Inject

/**
 * Handles fetching and caching GitHub users for Paging 3.
 *
 * This class fetches user data from a remote source and stores it in a local database.
 * It works with Paging 3 to load data efficiently as the user scrolls.
 */
@OptIn(ExperimentalPagingApi::class)
class UserPagingSource @Inject constructor(
    private val userRemoteDataSource: IUserRemoteDataSource,
    private val db: GithubAdminDatabase,
    private val userDao: UserDao
) : RemoteMediator<Int, UserEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        Log.d("UserPagingSource", "loadType = $loadType")

        val loadKey = when(loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()?.id ?: 0
            }
        }

        val result = userRemoteDataSource.getUsers(loadKey)

        return when (result) {
            is ResultData.ApiError -> MediatorResult.Error(
                ApiRequestException(result.code!!, result.message)
            )
            is ResultData.OperationError -> MediatorResult.Error(
                result.exception
            )
            is ResultData.Success -> {
                cacheUsers(loadType = loadType, users = result.data.body.map { it.toUserEntity() })

                val next = extractNextSinceParameter(result.data.header["link"])

                MediatorResult.Success(
                    endOfPaginationReached = next == null
                )
            }
        }
    }

    /**
     * Determines the initial load behavior.
     *
     * If the database is empty, it triggers a network refresh. Otherwise, it uses existing data.
     */
    override suspend fun initialize(): InitializeAction {
        return if (userDao.getAllUsers().isEmpty()) InitializeAction.LAUNCH_INITIAL_REFRESH
        else InitializeAction.SKIP_INITIAL_REFRESH
    }

    /**
     * Saves users to the database.
     *
     * Clears the database on refresh, otherwise just inserts.
     */
    private suspend fun cacheUsers(loadType: LoadType, users: List<UserEntity>) {
        db.withTransaction {
            if (loadType == LoadType.REFRESH) {
                userDao.deleteAllUsers()
            }

            userDao.insertUsers(users)
        }
    }
}