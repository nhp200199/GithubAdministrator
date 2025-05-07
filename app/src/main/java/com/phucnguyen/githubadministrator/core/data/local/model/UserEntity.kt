package com.phucnguyen.githubadministrator.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.phucnguyen.githubadministrator.common.model.UserDetail

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val userName: String,
    val avatarUrl: String?,
    val htmlUrl: String?,
    val location: String?,
    val followers: Int,
    val following: Int,
    val detailFetched: Boolean = false
) {
    fun toUserDetail(): UserDetail {
        return UserDetail(
            id = id,
            userName = userName,
            avatarUrl = avatarUrl ?: "",
            landingPageUrl = htmlUrl ?: "",
            location = location ?: "",
            followers = followers,
            following = following
        )
    }
}