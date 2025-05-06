package com.phucnguyen.githubadministrator.common.di

import com.phucnguyen.githubadministrator.common.data.dataSource.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.data.dataSource.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindUserRemoteDataSource(
        userRemoteDataSource: UserRemoteDataSource
    ): IUserRemoteDataSource
}