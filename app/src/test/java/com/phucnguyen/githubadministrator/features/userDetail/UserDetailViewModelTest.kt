package com.phucnguyen.githubadministrator.features.userDetail

import app.cash.turbine.test
import com.phucnguyen.githubadministrator.CoroutineTestRule
import com.phucnguyen.githubadministrator.common.data.repository.IUserRepository
import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.dataTest.USER_DETAIL_MODEL
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDetailViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()
    private lateinit var SUT: UserDetailViewModel
    private lateinit var userRepository: IUserRepository

    @Before
    fun setup() {
        userRepository = mockk()
        SUT = UserDetailViewModel(userRepository, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun getUserDetail_success() = runTest {
        val expected = USER_DETAIL_MODEL
        coEvery { userRepository.getUserDetail(any()) } returns ResultData.Success(USER_DETAIL_MODEL)

        SUT.uiState.test {
            awaitItem()

            SUT.getUserDetail("test")
            assertThat(awaitItem(), `is`(instanceOf(UserDetailUIState.Loading::class.java)))
            val result = awaitItem()
            assertThat(result, `is`(instanceOf(UserDetailUIState.Success::class.java)))
            assertThat((result as UserDetailUIState.Success).data, equalTo(expected))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getUserDetail_noNetwork() = runTest {
        coEvery { userRepository.getUserDetail(any()) }
            .returns(ResultData.OperationError(NoNetworkConnectionException()))

        SUT.uiState.test {
            awaitItem()

            SUT.getUserDetail("test")
            assertThat(awaitItem(), `is`(instanceOf(UserDetailUIState.Loading::class.java)))
            val result = awaitItem()
            assertThat(result, `is`(instanceOf(UserDetailUIState.Error::class.java)))
            assertThat((result as UserDetailUIState.Error).message, equalTo(NoNetworkConnectionException().message))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getUserDetail_apiError() = runTest {
        val errorMessage = "Api Error"
        coEvery { userRepository.getUserDetail(any()) }
            .returns(ResultData.ApiError(errorMessage))

        SUT.uiState.test {
            awaitItem()

            SUT.getUserDetail("test")
            assertThat(awaitItem(), `is`(instanceOf(UserDetailUIState.Loading::class.java)))
            val result = awaitItem()
            assertThat(result, `is`(instanceOf(UserDetailUIState.Error::class.java)))
            assertThat((result as UserDetailUIState.Error).message, equalTo(errorMessage))
            cancelAndConsumeRemainingEvents()
        }
    }
}