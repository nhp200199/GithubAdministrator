package com.phucnguyen.githubadministrator.common.di

import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import com.phucnguyen.githubadministrator.common.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository
}