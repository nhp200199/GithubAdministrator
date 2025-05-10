package com.phucnguyen.githubadministrator.dataTest

import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO

// DTO
val USER_LIST_DTO = listOf<UserDTO>(
    UserDTO(1, "userName1", "avatarUrl1", "landingPageUrl1", "location1", 1, 1),
    UserDTO(2, "userName2", "avatarUrl2", "landingPageUrl2", "location2", 2, 2),
    UserDTO(3, "userName3", "avatarUrl3", "landingPageUrl3", "location3", 3, 3),
)

val USER_DTO = USER_LIST_DTO[0]

// Entity
val USER_LIST_ENTITY = USER_LIST_DTO.map {
    UserEntity(
        id = it.id,
        userName = it.login ?: "",
        avatarUrl = it.avatarUrl ?: "",
        htmlUrl = it.landingPageUrl ?: "",
        location = it.location ?: "",
        followers = it.followers ?: 0,
        following = it.following ?: 0,
        detailFetched = false
    )
}
val USER_ENTITY = USER_LIST_ENTITY[0]

// Model
val USER_OVERVIEW_LIST_MODEL = USER_LIST_DTO.map { it.toUserOverview() }

val USER_DETAIL_MODEL = USER_DTO.toUserDetail()