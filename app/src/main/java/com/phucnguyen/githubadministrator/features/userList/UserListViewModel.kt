package com.phucnguyen.githubadministrator.features.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {
    val usersPaginatedFlow = userRepository.getPagingUsers()
        .cachedIn(viewModelScope)
}