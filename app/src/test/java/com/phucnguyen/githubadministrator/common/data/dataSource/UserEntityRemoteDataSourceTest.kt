package com.phucnguyen.githubadministrator.common.data.dataSource

import com.phucnguyen.githubadministrator.core.data.remote.service.IUserService
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

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

//    @Test
//    fun getUsers_correctParametersPassed() = runTest {
//        val sinceSlot = slot<Int>()
//        val perPageSlot = slot<Int>()
//        coEvery { userService.getUsers(capture(sinceSlot), capture(perPageSlot)) }
//            .returns(Result.Success(NetworkResponse(Headers.headersOf(), USER_LIST_DTO))
//            )
//
//        SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)
//
//        assertThat(sinceSlot.captured, equalTo(0))
//        assertThat(perPageSlot.captured, equalTo(20))
//    }
//
//    @Test
//    fun getUsers_success_userListReturned() = runTest {
//        coEvery { userService.getUsers(any(), any()) }
//            .returns(Result.Success(NetworkResponse(Headers.headersOf(), USER_LIST_DTO))
//            )
//
//        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)
//
//        assertThat(result, `is`(instanceOf(Result.Success::class.java)))
//    }
//
//    @Test
//    fun getUsers_noNetwork_networkErrorReturned() = runTest {
//        coEvery { userService.getUsers(any(), any()) }
//            .returns(
//                Result.OperationError(NoNetworkConnectionException())
//            )
//
//        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)
//
//        assertThat(result, `is`(instanceOf(Result.OperationError::class.java)))
//        val operationError = result as com.phucnguyen.githubadministrator.core.data.ResultData.Result.OperationError
//        assertThat(operationError.exception, `is`(instanceOf(NoNetworkConnectionException::class.java)))
//    }
//
//    @Test
//    fun getUsers_unknownError_unknownErrorReturned() = runTest {
//        coEvery { userService.getUsers(any(), any()) }
//            .returns(
//                Result.OperationError(UnknownException())
//            )
//
//        val result = SUT.getUsers(PARAM_SINCE, PARAM_PAGE_LIMIT)
//
//        assertThat(result, `is`(instanceOf(Result.OperationError::class.java)))
//        val operationError = result as com.phucnguyen.githubadministrator.core.data.ResultData.Result.OperationError
//        assertThat(operationError.exception, `is`(instanceOf(UnknownException::class.java)))
//    }
//
//    @Test
//    fun getUsers_correctUserNamePassed() = runTest {
//        val userNameSlot = slot<String>()
//        coEvery { userService.getUserDetail(capture(userNameSlot)) }
//            .returns(Result.Success(NetworkResponse(Headers.headersOf(), USER_DTO))
//            )
//
//        SUT.getUserDetail(PARAM_USER_NAME)
//
//        assertThat(userNameSlot.captured, equalTo(PARAM_USER_NAME))
//    }
//
//    @Test
//    fun getUserDetail_success_userDetailReturned() = runTest {
//        coEvery { userService.getUserDetail(any()) }
//            .returns(Result.Success(NetworkResponse(Headers.headersOf(), USER_DTO))
//            )
//
//        val result = SUT.getUserDetail(PARAM_USER_NAME)
//
//        assertThat(result, `is`(instanceOf(Result.Success::class.java)))
//    }
//
//    @Test
//    fun getUserDetail_noNetwork_networkErrorReturned() = runTest {
//        coEvery { userService.getUserDetail(any()) }
//            .returns(
//                Result.OperationError(NoNetworkConnectionException())
//            )
//
//        val result = SUT.getUserDetail(PARAM_USER_NAME)
//
//        assertThat(result, `is`(instanceOf(Result.OperationError::class.java)))
//        val operationError = result as com.phucnguyen.githubadministrator.core.data.ResultData.Result.OperationError
//        assertThat(operationError.exception, `is`(instanceOf(NoNetworkConnectionException::class.java)))
//    }
//
//    @Test
//    fun getUserDetail_unknownError_unknownErrorReturned() = runTest {
//        coEvery { userService.getUserDetail(any()) }
//            .returns(
//                Result.OperationError(UnknownException())
//            )
//
//        val result = SUT.getUserDetail(PARAM_USER_NAME)
//
//        assertThat(result, `is`(instanceOf(Result.OperationError::class.java)))
//        val operationError = result as com.phucnguyen.githubadministrator.core.data.ResultData.Result.OperationError
//        assertThat(operationError.exception, `is`(instanceOf(UnknownException::class.java)))
//    }
//
//    @Test
//    fun getUserDetail_userNotFound_userNotFoundReturned() = runTest {
//        coEvery { userService.getUserDetail(any()) }
//            .returns(
//                Result.ApiError(ErrorResponse("User not found"), 404)
//            )
//
//        val result = SUT.getUserDetail(PARAM_USER_NAME)
//
//        assertThat(result, `is`(instanceOf(Result.ApiError::class.java)))
//        val apiError = result as com.phucnguyen.githubadministrator.core.data.ResultData.Result.ApiError
//        assertThat(apiError.code, equalTo(404))
//    }
}