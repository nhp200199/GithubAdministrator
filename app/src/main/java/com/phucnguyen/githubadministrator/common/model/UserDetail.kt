package com.phucnguyen.githubadministrator.common.model

import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

data class UserDetail(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val landingPageUrl: String,
    val location: String,
    val followers: Int,
    val following: Int,
) {
    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            userName = userName,
            avatarUrl = avatarUrl,
            htmlUrl = landingPageUrl,
            location = location,
            followers = followers,
            following = following,
        )
    }
}