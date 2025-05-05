package com.phucnguyen.githubadministrator.core.data.remote.retrofit

import android.content.Context
import com.phucnguyen.githubadministrator.core.networkManager.NetworkManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientBuilder {
    companion object {
        fun default(context: Context): OkHttpClient.Builder {
            val networkMonitorInterceptor = NetworkMonitorInterceptor(NetworkManager(context))

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            builder.addInterceptor(logging)
            builder.addInterceptor(networkMonitorInterceptor)
            builder.addInterceptor(Interceptor { chain ->
                val newRequestBuilder = chain.request().newBuilder()
                chain.proceed(newRequestBuilder.build())
            })

            return builder
        }
    }
}