package com.phucnguyen.githubadministrator.common.data.dataSource.remote

import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.exception.UnknownException
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import com.phucnguyen.githubadministrator.dataTest.USER_DTO
import com.phucnguyen.githubadministrator.dataTest.USER_LIST_DTO
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class UserRemoteDataSourceTest {
    private val PARAM_SINCE = 0
    private val PARAM_PAGE_LIMIT = 20
    private val PARAM_USER_NAME = "test"

    private lateinit var SUT: UserRemoteDataSource
    private lateinit var userService: IUserService

    @Before
    fun setUp() {
        userService = mockk<IUserService>()
        SUT = UserRemoteDataSource(userService)
    }

    @Test
    fun getUsers_correctParametersPassed() = runTest {
        val sinceSlot = slot<Int>()
        val perPageSlot = slot<Int>()
        coEvery { userService.getUsers(capture(sinceSlot), capture(perPageSlot)) }
            .returns(
                Response.success(USER_LIST_DTO)
            )

        SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)

        assertThat(sinceSlot.captured, equalTo(0))
        assertThat(perPageSlot.captured, equalTo(20))
    }

    @Test
    fun getUsers_success_userListReturned() = runTest {
        coEvery { userService.getUsers(any(), any()) }
            .returns(Response.success(USER_LIST_DTO))

        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)

        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getUsers_noNetwork_networkErrorReturned() = runTest {
        coEvery { userService.getUsers(any(), any()) } throws IOException()

        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationError = result as ResultData.OperationError
        assertThat(operationError.exception, `is`(instanceOf(NoNetworkConnectionException::class.java)))
    }

    @Test
    fun getUsers_unknownError_unknownErrorReturned() = runTest {
        coEvery { userService.getUsers(any(), any()) } throws Exception()

        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationError = result as ResultData.OperationError
        assertThat(operationError.exception, `is`(instanceOf(UnknownException::class.java)))
    }

    @Test
    fun getUsers_correctUserNamePassed() = runTest {
        val userNameSlot = slot<String>()
        coEvery { userService.getUserDetail(capture(userNameSlot)) }
            .returns(Response.success(USER_DTO))

        SUT.getUserDetail(PARAM_USER_NAME)

        assertThat(userNameSlot.captured, equalTo(PARAM_USER_NAME))
    }

    @Test
    fun getUserDetail_success_userDetailReturned() = runTest {
        coEvery { userService.getUserDetail(any()) }
            .returns(Response.success(USER_DTO))

        val result = SUT.getUserDetail(PARAM_USER_NAME)

        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getUserDetail_noNetwork_networkErrorReturned() = runTest {
        coEvery { userService.getUserDetail(any()) } throws IOException()

        val result = SUT.getUserDetail(PARAM_USER_NAME)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationError = result as ResultData.OperationError
        assertThat(operationError.exception, `is`(instanceOf(NoNetworkConnectionException::class.java)))
    }

    @Test
    fun getUserDetail_unknownError_unknownErrorReturned() = runTest {
        coEvery { userService.getUserDetail(any()) } throws Exception()

        val result = SUT.getUserDetail(PARAM_USER_NAME)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationError = result as ResultData.OperationError
        assertThat(operationError.exception, `is`(instanceOf(UnknownException::class.java)))
    }

    @Test
    fun getUserDetail_userNotFound_userNotFoundReturned() = runTest {
        coEvery { userService.getUserDetail(any()) }
            .returns(Response.error(404, "".toResponseBody(null)))

        val result = SUT.getUserDetail(PARAM_USER_NAME)

        assertThat(result, `is`(instanceOf(ResultData.ApiError::class.java)))
        val apiError = result as ResultData.ApiError
        assertThat(apiError.code, equalTo(404))
    }
}