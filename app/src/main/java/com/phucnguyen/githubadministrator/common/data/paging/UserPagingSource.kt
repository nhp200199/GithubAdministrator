package com.phucnguyen.githubadministrator.common.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.phucnguyen.githubadministrator.common.data.dataSource.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.Result

class UserPagingSource(
    private val userRemoteDataSource: IUserRemoteDataSource
) : PagingSource<Int, UserOverview>() {

    override fun getRefreshKey(state: PagingState<Int, UserOverview>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserOverview> {
        val since = params.key ?: 0

        val result = userRemoteDataSource.getUsers(since = since)
        return when (result) {
            is Result.ApiError -> TODO()
            is Result.OperationError -> TODO()
            is Result.Success -> LoadResult.Page(
                data = result.body,
                prevKey = null, // because we only paging forward
                nextKey = 12
            )
        }
    }
}