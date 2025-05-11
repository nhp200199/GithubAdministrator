package com.phucnguyen.githubadministrator.common.exception

class ApiRequestException(
    val code: Int,
    val content: String
) : Exception()