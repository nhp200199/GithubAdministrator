package com.phucnguyen.githubadministrator.features.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import com.phucnguyen.githubadministrator.core.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserDetailUIState {
    data object Initial : UserDetailUIState()
    data object Loading : UserDetailUIState()
    data object Success : UserDetailUIState()
    data class Error(val message: String) : UserDetailUIState()
}

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: IUserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UserDetailUIState>(UserDetailUIState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getUserDetail(userName: String) {
        viewModelScope.launch {
            _uiState.value = UserDetailUIState.Loading

            val response = userRepository.getUserDetail(userName)

            when (response) {
                is Result.OperationError -> _uiState.value = UserDetailUIState.Error(response.exception.message ?: "")
                is Result.Success -> _uiState.value = UserDetailUIState.Success
                is Result.ApiError -> _uiState.value = UserDetailUIState.Error(response.body.message)
            }
        }
    }
}