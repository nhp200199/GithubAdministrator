package com.phucnguyen.githubadministrator.common.exception

import com.phucnguyen.githubadministrator.core.data.remote.ErrorResponse

class ApiRequestException(
    val code: Int,
    val content: String
) : Exception()