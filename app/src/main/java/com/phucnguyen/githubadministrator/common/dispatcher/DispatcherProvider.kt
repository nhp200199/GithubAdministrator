package com.phucnguyen.githubadministrator.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatcherProvider {
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun unconfined(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}