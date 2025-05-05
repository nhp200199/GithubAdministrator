package com.phucnguyen.githubadministrator.core.data.remote.retrofit

import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.exception.UnknownException
import com.phucnguyen.githubadministrator.core.data.Result
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class NetworkCall<T: Any, E: Any>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<Result<T, E>> {
    override fun enqueue(callback: Callback<Result<T, E>>) {
         delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(this@NetworkCall, Response.success(Result.Success(body)))
                    }
                    //`else` is optional here. Because there are cases where successful request
                    //does not return anything, just success code, eg: 201
                    else {
                        callback.onResponse(this@NetworkCall, Response.success(
                            Result.OperationError(
                            UnknownException()
                        )))
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (errorBody != null) {
                        callback.onResponse(this@NetworkCall, Response.success(Result.ApiError(errorBody, code)))
                    } else {
                        callback.onResponse(this@NetworkCall, Response.success(Result.OperationError(UnknownException())))
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse = when (t) {
                    is IOException -> Result.OperationError(NoNetworkConnectionException())
                    else -> Result.OperationError(UnknownException())
                }
                callback.onResponse(this@NetworkCall, Response.success(networkResponse))
            }

        })
    }

    override fun clone(): Call<Result<T, E>> {
        return NetworkCall(delegate.clone(), errorConverter)
    }

    override fun execute(): Response<Result<T, E>> = throw UnsupportedOperationException("MyCall doesn't support execute()")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}