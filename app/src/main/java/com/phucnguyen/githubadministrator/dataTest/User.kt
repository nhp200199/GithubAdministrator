package com.phucnguyen.githubadministrator.dataTest

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO

// DTO
val USER_LIST_DTO = listOf<UserDTO>(
    UserDTO(1, "userName1", "avatarUrl1", "landingPageUrl1", "location1", 1, 1),
    UserDTO(2, "userName2", "avatarUrl2", "landingPageUrl2", "location2", 2, 2),
    UserDTO(3, "userName3", "avatarUrl3", "landingPageUrl3", "location3", 3, 3),
)

val USER_DTO = USER_LIST_DTO[0]

// Model
val USER_DETAIL_MODEL = UserDetail(
    id = USER_DTO.id,
    userName = USER_DTO.login!!,
    avatarUrl = USER_DTO.avatarUrl!!,
    landingPageUrl = USER_DTO.landingPageUrl!!,
    location = USER_DTO.location!!,
    followers = USER_DTO.followers!!,
    following = USER_DTO.following!!
)