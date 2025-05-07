package com.phucnguyen.githubadministrator.core.data.remote.model

import com.google.gson.annotations.SerializedName
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview

data class UserDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("html_url")
    val landingPageUrl: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("followers")
    val followers: Int?,
    @SerializedName("following")
    val following: Int?,
) {
    fun toUserOverview() = UserOverview(
        id = id,
        userName = login ?: "",
        avatarUrl = avatarUrl ?: "",
        landingPageUrl = landingPageUrl ?: ""
    )

    fun toUserDetail() = UserDetail(
        id = id,
        userName = login ?: "",
        avatarUrl = avatarUrl ?: "",
        landingPageUrl = landingPageUrl ?: "",
        location = location ?: "",
        followers = followers ?: 0,
        following = following ?: 0
    )
}
