package com.phucnguyen.githubadministrator.common.di

import android.content.Context
import com.phucnguyen.githubadministrator.BuildConfig
import com.phucnguyen.githubadministrator.core.data.remote.retrofit.NetworkMonitorInterceptor
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import com.phucnguyen.githubadministrator.core.networkManager.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    companion object {
        @Provides
        @Singleton
        fun retrofit(
            okHttpClient: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun userService(retrofit: Retrofit): IUserService =
            retrofit.create(IUserService::class.java)

        @Provides
        fun OkHttpClient(
            @ApplicationContext context: Context,
        ): OkHttpClient {
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

            return builder.build()
        }
    }
}