package com.phucnguyen.githubadministrator.dataTest

import com.phucnguyen.githubadministrator.core.data.remote.model.UserDTO

val USER_LIST_DTO = listOf<UserDTO>(
    UserDTO(1, "userName1", "avatarUrl1", "landingPageUrl1"),
    UserDTO(2, "userName2", "avatarUrl2", "landingPageUrl2"),
    UserDTO(3, "userName3", "avatarUrl3", "landingPageUrl3"),
)

val USER_DTO = USER_LIST_DTO[0]