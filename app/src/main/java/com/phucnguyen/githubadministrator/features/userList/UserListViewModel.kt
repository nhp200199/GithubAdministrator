package com.phucnguyen.githubadministrator.features.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val pager: Pager<Int, UserEntity>
) : ViewModel() {
    val usersPaginatedFlow =
        pager.flow
            .map {
                it.map { userEntity -> userEntity.toUserOverview() }
            }
        .cachedIn(viewModelScope)
}