package com.phucnguyen.githubadministrator.core.data.remote.service

import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse
import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IUserService {
    @GET("users")
    suspend fun getUsers(@Query("since") since: Long, @Query("per_page") perPage: Int): Result<List<UserDTO>, ErrorResponse>
    @GET("user/{userName}")
    suspend fun getUserDetail(@Path("userName") userName: String): Result<UserDTO, ErrorResponse>
}