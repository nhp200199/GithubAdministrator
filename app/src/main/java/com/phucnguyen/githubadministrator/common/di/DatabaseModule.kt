package com.phucnguyen.githubadministrator.common.di

import android.content.Context
import androidx.room.Room
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.room.DB_NAME
import com.phucnguyen.githubadministrator.core.data.local.room.GithubAdminDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GithubAdminDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            GithubAdminDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: GithubAdminDatabase): UserDao {
        return database.userDao()
    }
}