package com.phucnguyen.githubadministrator.common.data.dataSource

import android.util.Log
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.common.utils.safeApiCall
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: IUserService
) : IUserRemoteDataSource {
    override suspend fun getUsers(
        since: Int,
        perPage: Int
    ): ResultData<List<UserOverview>> {
        val result = safeApiCall { userService.getUsers(since, perPage) }

        //TODO: handle 304 code
        return when (result) {
            is ResultData.Success -> ResultData.Success(
//                NetworkResponse(
//                    result.data.header,
//                    result.data.body.map { it.toUserOverview() }
//                )
                result.data.map { it.toUserOverview() }
            )
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }

    override suspend fun getUserDetail(userName: String): ResultData<UserDetail> {
        val result = safeApiCall { userService.getUserDetail(userName) }
        Log.d("Phuc", "getUserDetail: $result")

        return when (result) {
            is ResultData.Success -> ResultData.Success(
//                NetworkResponse(
//                    result.data.header,
//                    result.data.body.toUserDetail()
//                )

                result.data.toUserDetail()
            )
            is ResultData.ApiError -> result
            is ResultData.OperationError -> result
        }
    }
}