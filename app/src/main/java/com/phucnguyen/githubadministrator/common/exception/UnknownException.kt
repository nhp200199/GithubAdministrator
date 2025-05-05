package com.phucnguyen.githubadministrator.common.exception

class UnknownException: Exception() {
    override val message: String
        get() = "Unknown error occurs"
}