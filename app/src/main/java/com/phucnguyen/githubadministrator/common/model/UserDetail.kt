package com.phucnguyen.githubadministrator.common.model

data class UserDetail(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val landingPageUrl: String,
    val location: String,
    val followers: Int,
    val following: Int,
)