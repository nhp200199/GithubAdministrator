package com.phucnguyen.githubadministrator.common.data.repository

import com.phucnguyen.githubadministrator.common.data.dataSource.local.IUserLocalDataSource
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import com.phucnguyen.githubadministrator.dataTest.USER_DETAIL_MODEL
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    private lateinit var SUT: UserRepository
    private val userLocalDataSource: IUserLocalDataSource = mockk()
    private val userRemoteDataSource: IUserRemoteDataSource = mockk()

    @Before
    fun setUp() {
        SUT = UserRepository(userLocalDataSource, userRemoteDataSource)
    }

    @Test
    fun getUserDetail_localReturnsSuccess_returnsLocalSuccessResult() = runTest {
        val userName = "John Doe"
        val localUserDetail = USER_DETAIL_MODEL
        val localSuccessResult = ResultData.Success(localUserDetail)

        coEvery { userLocalDataSource.getUserByName(any()) } returns localSuccessResult

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify(exactly = 0) { userRemoteDataSource.getUserDetail(any()) }
        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((result as ResultData.Success).data, equalTo(localUserDetail))
    }

    @Test
    fun getUserDetail_localReturnsApiError_remoteReturnsSuccess_updatesLocalAndReturnsRemoteSuccess() = runTest {
        val userName = "John Doe"
        val localApiError = ResultData.ApiError("User detail not fetched")
        val remoteUserDetail = USER_DETAIL_MODEL
        val remoteSuccessResult = ResultData.Success(NetworkResponse(Headers.headersOf(), remoteUserDetail))
        val userEntityToUpdate = remoteUserDetail.toUserEntity().copy(detailFetched = true)
        val localUpdateSuccess = ResultData.Success(Unit)

        coEvery { userLocalDataSource.getUserByName(userName) } returns localApiError
        coEvery { userRemoteDataSource.getUserDetail(userName) } returns remoteSuccessResult
        coEvery { userLocalDataSource.updateUser(userEntityToUpdate) } returns localUpdateSuccess

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify { userRemoteDataSource.getUserDetail(userName) }
        coVerify { userLocalDataSource.updateUser(userEntityToUpdate) }
        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((result as ResultData.Success).data, equalTo(remoteUserDetail))
    }

    @Test
    fun getUserDetail_localReturnsOperationError_remoteReturnsSuccess_updatesLocalAndReturnsRemoteSuccess() = runTest {
        val userName = "John Doe"
        val localOperationError = ResultData.OperationError(Exception("Local DB error"))
        val remoteUserDetail = USER_DETAIL_MODEL
        val remoteSuccessResult = ResultData.Success(NetworkResponse(Headers.headersOf(), remoteUserDetail))
        val userEntityToUpdate = remoteUserDetail.toUserEntity().copy(detailFetched = true)
        val localUpdateSuccess = ResultData.Success(Unit)

        coEvery { userLocalDataSource.getUserByName(userName) } returns localOperationError
        coEvery { userRemoteDataSource.getUserDetail(userName) } returns remoteSuccessResult
        coEvery { userLocalDataSource.updateUser(userEntityToUpdate) } returns localUpdateSuccess

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify { userRemoteDataSource.getUserDetail(userName) }
        coVerify { userLocalDataSource.updateUser(userEntityToUpdate) }
        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((result as ResultData.Success).data, equalTo(remoteUserDetail))
    }

    @Test
    fun getUserDetail_localReturnsApiError_remoteReturnsApiError_returnsRemoteApiError() =
        runBlocking {
        val userName = "John Doe"
        val localApiError = ResultData.ApiError("User detail not fetched")
        val remoteApiError = ResultData.ApiError("User not found", 404)

        coEvery { userLocalDataSource.getUserByName(userName) } returns localApiError
        coEvery { userRemoteDataSource.getUserDetail(userName) } returns remoteApiError

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify { userRemoteDataSource.getUserDetail(userName) }
        coVerify(exactly = 0) { userLocalDataSource.updateUser(any()) }
        assertThat(result, `is`(instanceOf(ResultData.ApiError::class.java)))
        assertThat((result as ResultData.ApiError).code, equalTo(404))
        assertThat(result.message, equalTo("User not found"))
    }

    @Test
    fun getUserDetail_localReturnsOperationError_remoteReturnsApiError_returnsRemoteApiError() = runTest {
        val userName = "John Doe"
        val localOperationError = ResultData.OperationError(Exception("Local DB error"))
        val remoteApiError = ResultData.ApiError("User not found", 404)

        coEvery { userLocalDataSource.getUserByName(userName) } returns localOperationError
        coEvery { userRemoteDataSource.getUserDetail(userName) } returns remoteApiError

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify { userRemoteDataSource.getUserDetail(userName) }
        coVerify(exactly = 0) { userLocalDataSource.updateUser(any()) }
        assertThat(result, instanceOf(ResultData.ApiError::class.java))
        assertThat((result as ResultData.ApiError).code, equalTo(404))
        assertThat(result.message, equalTo("User not found"))
    }

    @Test
    fun getUserDetail_localReturnsApiError_remoteReturnsOperationError_returnsRemoteOperationError() = runTest {
        val userName = "John Doe"
        val localApiError = ResultData.ApiError("User detail not fetched")
        val remoteOperationError = ResultData.OperationError(Exception("Network error"))

        coEvery { userLocalDataSource.getUserByName(userName) } returns localApiError
        coEvery { userRemoteDataSource.getUserDetail(userName) } returns remoteOperationError

        val result = SUT.getUserDetail(userName)

        coVerify { userLocalDataSource.getUserByName(userName) }
        coVerify { userRemoteDataSource.getUserDetail(userName) }
        coVerify(exactly = 0) { userLocalDataSource.updateUser(any()) }
        assertThat(result, instanceOf(ResultData.OperationError::class.java))
    }
}