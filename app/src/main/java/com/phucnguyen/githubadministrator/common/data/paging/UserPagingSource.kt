package com.phucnguyen.githubadministrator.common.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.phucnguyen.githubadministrator.common.data.dataSource.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.exception.ApiRequestException
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.common.utils.extractNextSinceParameter
import com.phucnguyen.githubadministrator.core.data.Result
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val userRemoteDataSource: IUserRemoteDataSource
) : PagingSource<Int, UserOverview>() {

    override fun getRefreshKey(state: PagingState<Int, UserOverview>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserOverview> {
        val since = params.key ?: 0

        val result = userRemoteDataSource.getUsers(since = since)
        return when (result) {
            is Result.ApiError -> LoadResult.Error(
                ApiRequestException(result.code!!, result.body)
            )
            is Result.OperationError -> LoadResult.Error(
                result.exception
            )
            is Result.Success -> {
                val next = extractNextSinceParameter(result.data.header["links"])

                LoadResult.Page(
                    data = result.data.body,
                    prevKey = null, // because we only paging forward
                    nextKey = next
                )
            }
        }
    }
}