package com.phucnguyen.githubadministrator.common.exception

import java.io.IOException

class NoNetworkConnectionException: IOException() {
    override val message: String
        get() = "No internet connection"
}