package com.phucnguyen.githubadministrator.dataTest

import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO

// DTO
val USER_LIST_DTO = listOf<UserDTO>(
    UserDTO(1, "userName1", "avatarUrl1", "landingPageUrl1", "location1", 1, 1),
    UserDTO(2, "userName2", "avatarUrl2", "landingPageUrl2", "location2", 2, 2),
    UserDTO(3, "userName3", "avatarUrl3", "landingPageUrl3", "location3", 3, 3),
)

val USER_DTO = USER_LIST_DTO[0]

// Model
val USER_LIST_MODEL = listOf(
    UserOverview(1, "userName1", "avatarUrl1", "landingPageUrl1"),
    UserOverview(2, "userName2", "avatarUrl2", "landingPageUrl2"),
    UserOverview(3, "userName3", "avatarUrl3", "landingPageUrl3"),
    UserOverview(4, "userName4", "avatarUrl4", "landingPageUrl4"),
    UserOverview(5, "userName5", "avatarUrl5", "landingPageUrl5"),
    UserOverview(6, "userName6", "avatarUrl6", "landingPageUrl6"),
    UserOverview(7, "userName7", "avatarUrl7", "landingPageUrl7"),
    UserOverview(8, "userName8", "avatarUrl8", "landingPageUrl8"),
    UserOverview(9, "userName9", "avatarUrl9", "landingPageUrl9"),
    UserOverview(10, "userName10", "avatarUrl10", "landingPageUrl10"),
)

val USER_DETAIL_MODEL = UserDetail(
    id = USER_DTO.id,
    userName = USER_DTO.login!!,
    avatarUrl = USER_DTO.avatarUrl!!,
    landingPageUrl = USER_DTO.landingPageUrl!!,
    location = USER_DTO.location!!,
    followers = USER_DTO.followers!!,
    following = USER_DTO.following!!
)