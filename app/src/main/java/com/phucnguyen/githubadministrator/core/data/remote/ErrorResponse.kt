package com.phucnguyen.githubadministrator.core.data.remote

import kotlinx.serialization.SerialName

data class ErrorResponse(
    @SerialName("message")
    val message: String
)