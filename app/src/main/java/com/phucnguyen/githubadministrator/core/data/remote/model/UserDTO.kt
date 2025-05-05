package com.phucnguyen.githubadministrator.core.data.remote.model

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import kotlinx.serialization.SerialName

data class UserDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val userName: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("html_url")
    val landingPageUrl: String
) {
    fun toUserOverview() = UserOverview(
        id = id,
        userName = userName,
        avatarUrl = avatarUrl,
        landingPageUrl = landingPageUrl
    )

    fun toUserDetail() = UserDetail(
        id = id,
        userName = userName,
        avatarUrl = avatarUrl,
        landingPageUrl = landingPageUrl,
        name = ""
    )
}
