package com.phucnguyen.githubadministrator.common.model

import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

data class UserOverview(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val landingPageUrl: String
) {
    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            userName = userName,
            avatarUrl = avatarUrl,
            htmlUrl = landingPageUrl,
            location = null,
            followers = 0,
            following = 0,
        )
    }
}
