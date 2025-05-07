package com.phucnguyen.githubadministrator.core.data.remote.service

import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IUserService {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<UserDTO>>

    @GET("users/{userName}")
    suspend fun getUserDetail(@Path("userName") userName: String): Response<UserDTO>
}