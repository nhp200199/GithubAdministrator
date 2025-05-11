package com.phucnguyen.githubadministrator.features.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import com.phucnguyen.githubadministrator.common.dispatcher.DispatcherProvider
import com.phucnguyen.githubadministrator.common.model.UserDetail
import com.phucnguyen.githubadministrator.core.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserDetailUIState {
    data object Initial : UserDetailUIState()
    data object Loading : UserDetailUIState()
    data class Success(val data: UserDetail) : UserDetailUIState()
    data class Error(val message: String) : UserDetailUIState()
}

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {
    private val _uiState = MutableStateFlow<UserDetailUIState>(UserDetailUIState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getUserDetail(userName: String) {
        _uiState.value = UserDetailUIState.Loading

        viewModelScope.launch(dispatcherProvider.main()) {
            val response = userRepository.getUserDetail(userName)

            when (response) {
                is ResultData.OperationError -> _uiState.value = UserDetailUIState.Error(response.exception.message ?: "")
                is ResultData.Success -> _uiState.value = UserDetailUIState.Success(response.data)
                is ResultData.ApiError -> _uiState.value = UserDetailUIState.Error(response.message)
            }
        }
    }
}