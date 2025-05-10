package com.phucnguyen.githubadministrator.common.data.dataSource.local

import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.dataTest.USER_ENTITY
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserLocalDataSourceTest {
    private lateinit var SUT: IUserLocalDataSource
    private val userDao: UserDao = mockk()

    @Before
    fun setUp() {
        SUT = UserLocalDataSource(userDao)
    }

    @Test
    fun updateUser_userDaoUpdateSuccessful_returnsSuccess() = runTest {
        val user = USER_ENTITY
        coEvery { userDao.updateUser(user) } returns Unit

        val result = SUT.updateUser(user)

        coVerify { userDao.updateUser(user) } // Verify the DAO method was called
        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun updateUser_userDaoThrowsException_returnsOperationError() = runTest {
        val user = USER_ENTITY
        val exception = Exception("Database error")
        coEvery { userDao.updateUser(any()) } throws exception

        val result = SUT.updateUser(user)

        coVerify { userDao.updateUser(user) }
        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        assertThat((result as ResultData.OperationError).exception, equalTo(exception))
    }

    @Test
    fun getUserByName_userDetailFetched_returnsSuccessWithUserDetail() = runTest {
        val userName = "John Doe"
        val userEntity = USER_ENTITY.copy(detailFetched = true)
        val expectedUserDetail = userEntity.toUserDetail()
        coEvery { userDao.getUserByName(any()) } returns userEntity

        val result = SUT.getUserByName(userName)

        coVerify { userDao.getUserByName(userName) }
        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((result as ResultData.Success).data, equalTo(expectedUserDetail))
    }

    @Test
    fun getUserByName_userDetailNotFetched_returnsApiError() = runTest {
        val userName = "John Doe"
        val userEntity = USER_ENTITY
        coEvery { userDao.getUserByName(any()) } returns userEntity

        val result = SUT.getUserByName(userName)

        coVerify { userDao.getUserByName(userName) }
        assertThat(result, `is`(instanceOf(ResultData.ApiError::class.java)))
        assertThat((result as ResultData.ApiError).message, equalTo("User detail not fetched"))
    }

    @Test
    fun getUserByName_userDaoThrowsException_returnsOperationError() = runTest {
        val userName = "John Doe"
        val exception = Exception("Database error")
        coEvery { userDao.getUserByName(any()) } throws exception

        val result = SUT.getUserByName(userName)

        coVerify { userDao.getUserByName(userName) }
        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        assertThat((result as ResultData.OperationError).exception, equalTo(exception))
    }
}