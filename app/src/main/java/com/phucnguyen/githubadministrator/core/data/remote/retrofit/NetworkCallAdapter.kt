package com.phucnguyen.githubadministrator.core.data.remote.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type
import com.phucnguyen.githubadministrator.core.data.Result
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse

class NetworkCallAdapter<T: Any, E: Any>(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, E>
) : CallAdapter<T, Call<Result<NetworkResponse<T>, E>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Result<NetworkResponse<T>, E>> {
        return NetworkCall(call, errorConverter)
    }
}