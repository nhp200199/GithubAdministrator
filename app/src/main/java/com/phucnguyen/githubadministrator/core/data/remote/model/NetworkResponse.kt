package com.phucnguyen.githubadministrator.core.data.remote.model

import okhttp3.Headers

data class NetworkResponse<T>(
    val header: Headers,
    val body: T
)
