package com.phucnguyen.githubadministrator.common.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.UserRemoteDataSource
import com.phucnguyen.githubadministrator.common.data.paging.UserPagingSource
import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import com.phucnguyen.githubadministrator.common.data.repository.UserRepository
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import com.phucnguyen.githubadministrator.core.data.local.room.GithubAdminDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserPager(
            githubAdminDatabase: GithubAdminDatabase,
            useRemoteDataSource: UserRemoteDataSource,
            userDao: UserDao
        ): Pager<Int, UserEntity> {
            return Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = UserPagingSource(
                    userRemoteDataSource = useRemoteDataSource,
                    db = githubAdminDatabase,
                    userDao = userDao
                ),
                pagingSourceFactory = {
                    userDao.getAllUsers()
                }
            )
        }
    }
}