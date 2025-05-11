package com.phucnguyen.githubadministrator.common.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
import com.phucnguyen.githubadministrator.common.exception.ApiRequestException
import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.model.UserOverview
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.room.GithubAdminDatabase
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import com.phucnguyen.githubadministrator.dataTest.USER_LIST_ENTITY
import com.phucnguyen.githubadministrator.dataTest.USER_OVERVIEW_LIST_MODEL
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalPagingApi::class)
class UserPagingSourceTest {
    private lateinit var SUT: UserPagingSource
    private lateinit var userRemoteDataSource: IUserRemoteDataSource
    private lateinit var db: GithubAdminDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        userRemoteDataSource = mockk()
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GithubAdminDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = db.userDao()

        SUT = UserPagingSource(userRemoteDataSource, db, userDao)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoad_success_userIsCached() = runTest {
        successLoadingUsersFromRemote(
            headers = Headers.Builder().add("Link", """<https://api.github.com/users?since=10>; rel="next""").build()
        )

        SUT.load(
            loadType = LoadType.REFRESH,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        val result = userDao.getAllUsers()
        assertThat(result.size, equalTo(USER_OVERVIEW_LIST_MODEL.size))
    }

    @Test
    fun refreshLoad_success_moreDataToLoad_mediatorSuccessReturnedWithEndOfPagingFalse() = runTest {
        successLoadingUsersFromRemote(
            headers = Headers.Builder().add("Link", """ <https://api.github.com/users?since=10>; rel="next" """).build()
        )

        val result = SUT.load(
            loadType = LoadType.REFRESH,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        assertThat(result, `is`(instanceOf(RemoteMediator.MediatorResult.Success::class.java)))
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(false))
    }

    @Test
    fun refreshLoad_success_noMoreDataToLoad_mediatorSuccessReturnedWithEndOfPagingFalse() = runTest {
        successLoadingUsersFromRemote(
            headers = Headers.Builder().add("Link", "").build()
        )

        val result = SUT.load(
            loadType = LoadType.REFRESH,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        assertThat(result, `is`(instanceOf(RemoteMediator.MediatorResult.Success::class.java)))
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(true))
    }

    @Test
    fun refreshLoad_noNetwork_mediatorErrorReturned() = runTest {
        coEvery { userRemoteDataSource.getUsers(any()) }
            .returns(ResultData.OperationError(NoNetworkConnectionException()))

        val result = SUT.load(
            loadType = LoadType.REFRESH,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        assertThat(result, `is`(instanceOf(RemoteMediator.MediatorResult.Error::class.java)))
        assertThat(
            (result as RemoteMediator.MediatorResult.Error).throwable,
            `is`(instanceOf(NoNetworkConnectionException::class.java))
        )
    }

    @Test
    fun refreshLoad_apiError_mediatorErrorReturned() = runTest {
        coEvery { userRemoteDataSource.getUsers(any()) }
            .returns(
                ResultData.ApiError(
                    message = "Something went wrong",
                    code = 400
                )
            )

        val result = SUT.load(
            loadType = LoadType.REFRESH,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        assertThat(result, `is`(instanceOf(RemoteMediator.MediatorResult.Error::class.java)))
        assertThat(
            (result as RemoteMediator.MediatorResult.Error).throwable,
            `is`(instanceOf(ApiRequestException::class.java))
        )
    }

    @Test
    fun prependLoad_mediatorSuccessReturnedWithEndOfPagingTrue() = runTest {
        val result = SUT.load(
            loadType = LoadType.PREPEND,
            state = PagingState(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(10),
                leadingPlaceholderCount = 10
            )
        )

        assertThat(result, `is`(instanceOf(RemoteMediator.MediatorResult.Success::class.java)))
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(true))
    }

    @Test
    fun initialize_noDataInDatabase_refreshValueReturned() = runTest {
        val result = SUT.initialize()

        assertThat(result, equalTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH))
    }

    @Test
    fun initialize_dataInDatabase_skipRefreshValueReturned() = runTest {
        userDao.insertUsers(USER_LIST_ENTITY)

        val result = SUT.initialize()

        assertThat(result, equalTo(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH))
    }

    private fun successLoadingUsersFromRemote(
        headers: Headers,
        body: List<UserOverview> = USER_OVERVIEW_LIST_MODEL
    ) {
        coEvery { userRemoteDataSource.getUsers(any()) }
            .returns(
                ResultData.Success(
                    NetworkResponse(
                        header = headers,
                        body = body
                    )
                )
            )
    }
}