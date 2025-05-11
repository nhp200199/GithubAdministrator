package com.phucnguyen.githubadministrator.common.di

import com.phucnguyen.githubadministrator.common.dispatcher.DefaultDispatcherProvider
import com.phucnguyen.githubadministrator.common.dispatcher.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {
    @Binds
    abstract fun defaultDispatcherProvider(defaultDispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}