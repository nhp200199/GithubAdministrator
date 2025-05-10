//package com.phucnguyen.githubadministrator.common.data.paging
//
//import androidx.compose.foundation.layout.add
//import androidx.paging.LoadType
//import androidx.paging.RemoteMediator
//import androidx.paging.map
//import androidx.room.withTransaction
//import com.phucnguyen.githubadministrator.common.data.dataSource.remote.IUserRemoteDataSource
//import com.phucnguyen.githubadministrator.core.data.ResultData
//import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
//import com.phucnguyen.githubadministrator.core.data.local.room.GithubAdminDatabase
//import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
//import com.phucnguyen.githubadministrator.dataTest.USER_LIST_DTO
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockk
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.runTest
//import okhttp3.Headers
//import org.junit.Before
//import org.junit.Test
//
//class UserPagingSourceTest {
//    private lateinit var SUT: UserPagingSource
//    private val userRemoteDataSource: IUserRemoteDataSource = mockk()
//    private val db: GithubAdminDatabase = mockk()
//    private val userDao: UserDao = mockk()
//
//    @Before
//    fun setUp() {
//        // Mock the withTransaction block in the database
//        coEvery { db.withTransaction<Unit>(any()) } coAnswers {
//            // Execute the lambda passed to withTransaction
//            val block = args[0] as suspend () -> Unit
//            block.invoke()
//        }
//        SUT = UserPagingSource(userRemoteDataSource, db, userDao)
//    }
//
//    @Test
//    fun load_loadTypeRefresh_fetchesAndCachesUsersAndReturnsSuccess() = runTest {
//        // Given
//        val usersDto = USER_LIST_DTO
//        val usersEntity = usersDto.map { it.toUserEntity() }
//        val headers = Headers.Builder().add("link", "<url>; rel=\"next\"").build()
//        val successResult = ResultData.Success(NetworkResponse(headers, usersDto))
//
//        coEvery { userRemoteDataSource.getUsers(0) } returns successResult
//        coEvery { userDao.deleteAllUsers() } returns Unit
//        coEvery { userDao.insertUsers(usersEntity) } returns Unit
//
//        val pagingState: PagingState<Int, UserEntity> = mockk {
//            every { lastItemOrNull() } returns null // For REFRESH, lastItemOrNull is not used for loadKey
//        }
//
//        // When
//        val result = SUT.load(LoadType.REFRESH, pagingState)
//
//        // Then
//        coVerify { userRemoteDataSource.getUsers(0) }
//        coVerify { userDao.deleteAllUsers() }
//        coVerify { userDao.insertUsers(usersEntity) }
//            com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
//            com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(false))
//    }
//
////    @Test
////    fun load_loadTypeRefresh_apiError_returnsError() = runBlocking {
////        // Given
////        val apiErrorResult = ResultData.ApiError<List<UserDto>>(401, "Unauthorized")
////
////        coEvery { userRemoteDataSource.getUsers(0) } returns apiErrorResult
////
////        val pagingState: PagingState<Int, UserEntity> = mockk {
////            every { lastItemOrNull() } returns null
////        }
////
////        // When
////        val result = SUT.load(LoadType.REFRESH, pagingState)
////
////        // Then
////        coVerify { userRemoteDataSource.getUsers(0) }
////        com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
////        com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Error).throwable, instanceOf(ApiRequestException::class.java))
////        com.google.common.truth.Truth.assertThat((result.throwable as ApiRequestException).code, equalTo(401))
////        com.google.common.truth.Truth.assertThat(result.throwable.message, equalTo("Unauthorized"))
////    }
////
////    @Test
////    fun load_loadTypeRefresh_operationError_returnsError() = runBlocking {
////        // Given
////        val exception = Exception("Network error")
////        val operationErrorResult = ResultData.OperationError<List<UserDto>>(exception)
////
////        coEvery { userRemoteDataSource.getUsers(0) } returns operationErrorResult
////
////        val pagingState: PagingState<Int, UserEntity> = mockk {
////            every { lastItemOrNull() } returns null
////        }
////
////        // When
////        val result = SUT.load(LoadType.REFRESH, pagingState)
////
////        // Then
////        coVerify { userRemoteDataSource.getUsers(0) }
////        com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Error::class.java))
////        com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Error).throwable, equalTo(exception))
////    }
////
////    @Test
////    fun load_loadTypePrepend_returnsSuccessEndOfPaginationReachedTrue() =
////        runBlocking {
////        // Given
////        val pagingState: PagingState<Int, UserEntity> = mockk() // PagingState is not used for PREPEND loadKey
////
////        // When
////        val result = SUT.load(LoadType.PREPEND, pagingState)
////
////        // Then
////            com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
////            com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(true))
////        coVerify(exactly = 0) { userRemoteDataSource.getUsers(any()) } // Ensure no network call for PREPEND
////    }
////
////    @Test
////    fun load_loadTypeAppend_lastItemNotNull_fetchesAndCachesUsersAndReturnsSuccess() =
////        runBlocking {
////        // Given
////        val lastItem = UserEntity(30, "lastUser", "url", "html", "country", 5, 2, true)
////        val usersDto = listOf(
////            UserDto(31, "user31", "url31", "html31"),
////            UserDto(32, "user32", "url32", "html32")
////        )
////        val usersEntity = usersDto.map { it.toUserEntity() }
////        val headers = Headers.Builder().add("link", "<url>; rel=\"next\"").build()
////        val successResult = ResultData.Success(ResultData.Success.Data(usersDto, headers))
////
////        coEvery { userRemoteDataSource.getUsers(lastItem.id) } returns successResult
////        coEvery { userDao.insertUsers(usersEntity) } returns Unit
////
////        val pagingState: PagingState<Int, UserEntity> = mockk {
////            every { lastItemOrNull() } returns lastItem
////        }
////
////        // When
////        val result = SUT.load(LoadType.APPEND, pagingState)
////
////        // Then
////        coVerify { userRemoteDataSource.getUsers(lastItem.id) }
////        coVerify(exactly = 0) { userDao.deleteAllUsers() } // Ensure deleteAllUsers is not called for APPEND
////        coVerify { userDao.insertUsers(usersEntity) }
////            com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
////            com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(false))
////    }
////
////    @Test
////    fun load_loadTypeAppend_lastItemNull_returnsSuccessEndOfPaginationReachedTrue() =
////        runBlocking {
////        // Given
////        val pagingState: PagingState<Int, UserEntity> = mockk {
////            every { lastItemOrNull() } returns null
////        }
////
////        // When
////        val result = SUT.load(LoadType.APPEND, pagingState)
////
////        // Then
////            com.google.common.truth.Truth.assertThat(result, instanceOf(RemoteMediator.MediatorResult.Success::class.java))
////            com.google.common.truth.Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(true))
////        coVerify(exactly = 0) { userRemoteDataSource.getUsers(any()) } // Ensure no network call when lastItem is null for APPEND
////    }
//}