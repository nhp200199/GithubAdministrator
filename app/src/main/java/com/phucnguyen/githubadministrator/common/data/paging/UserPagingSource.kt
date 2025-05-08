package com.phucnguyen.githubadministrator.common.data.paging

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

@OptIn(ExperimentalPagingApi::class)
class UserPagingSource @Inject constructor(
    private val userRemoteDataSource: IUserRemoteDataSource,
    private val db: GithubAdminDatabase,
    private val userDao: UserDao
) : RemoteMediator<Int, UserEntity>() {

    //    override fun getRefreshKey(state: PagingState<Int, UserOverview>): Int? {
//        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserOverview> {
//        val since = params.key ?: 0
//
//        val result = userRemoteDataSource.getUsers(since = since)
//        return when (result) {
//            is ResultData.ApiError -> LoadResult.Error(
//                ApiRequestException(result.code!!, result.message)
//            )
//            is ResultData.OperationError -> LoadResult.Error(
//                result.exception
//            )
//            is ResultData.Success -> {
//                val next = extractNextSinceParameter(result.data.header["link"])
//
//                LoadResult.Page(
//                    data = result.data.body,
//                    prevKey = null, // because we only paging forward
//                    nextKey = next
//                )
//            }
//        }
//    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val loadKey = when(loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                lastItem.id
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

    private suspend fun cacheUsers(loadType: LoadType, users: List<UserEntity>) {
            if (loadType == LoadType.REFRESH) {
                userDao.deleteAllUsers()
            }

            userDao.insertUsers(users)
//        db.withTransaction {
//        }
    }
}