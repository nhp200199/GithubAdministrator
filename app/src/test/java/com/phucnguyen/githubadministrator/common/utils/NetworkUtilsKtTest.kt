package com.phucnguyen.githubadministrator.common.utils

import com.phucnguyen.githubadministrator.common.exception.NoNetworkConnectionException
import com.phucnguyen.githubadministrator.common.exception.UnknownException
import com.phucnguyen.githubadministrator.core.data.ResultData
import com.phucnguyen.githubadministrator.core.data.remote.model.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class NetworkUtilsTest {

    @Test
    fun extractNextSinceParameter_nextLink_returnCorrectSinceValue() {
        val linkHeader = """<https://api.github.com/users?since=46>; rel="next", <https://api.github.com/users{?since}>; rel="first""""
        val expectedSince = 46
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(expectedSince))
    }

    @Test
    fun extractNextSinceParameter_nextLinkNotPresent_returnNull() {
        val linkHeader = """<https://api.github.com/users{?since}>; rel="first", <https://api.github.com/users?page=2>; rel="last""""
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(null))
    }

    @Test
    fun extractNextSinceParameter_sinceParameterNotPresent_returnNull() {
        val linkHeader = """<https://api.github.com/users?page=2>; rel="next", <https://api.github.com/users{?since}>; rel="first""""
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(null))
    }

    @Test
    fun extractNextSinceParameter_multipleLinks_returnSinceValue() {
        val linkHeader = """<https://api.github.com/users?since=10>; rel="prev", <https://api.github.com/users?since=46>; rel="next", <https://api.github.com/users{?since}>; rel="first""""
        val expectedSince = 46
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(expectedSince))
    }

    @Test
    fun extractNextSinceParameter_extraSpacesInLinkHeader_returnSinceValue() {
        val linkHeader = """ <https://api.github.com/users?since=46> ; rel="next" , <https://api.github.com/users{?since}> ; rel="first" """
        val expectedSince = 46
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(expectedSince))
    }

    @Test
    fun extractNextSinceParameter_emptyLinkHeader_returnNull() {
        val linkHeader = ""
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(null))
    }

    @Test
    fun extractNextSinceParameter_invalidLinkHeaderFormat_returnNull() {
        val linkHeader = "invalid-link-header"
        val actualSince = extractNextSinceParameter(linkHeader)
        assertThat(actualSince, equalTo(null))
    }

    @Test
    fun safeApiCall_apiCallSuccessfulWithBody_returnsSuccessWithNetworkResponse() = runTest {
        val mockBody = "Success Data"
        val mockHeaders = Headers.Builder().add("Content-Type", "text/plain").build()
        val mockResponse = Response.success(mockBody, mockHeaders)
        val apiCall: suspend () -> Response<String> = mockk()

        coEvery { apiCall.invoke() } returns mockResponse

        val result = safeApiCall(apiCall)

        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
        val successResult = result as ResultData.Success
        assertThat(successResult.data, instanceOf(NetworkResponse::class.java))
        assertThat(successResult.data.body, equalTo(mockBody))
        assertThat(successResult.data.header, equalTo(mockHeaders))
    }

    @Test
    fun safeApiCall_apiCallSuccessfulWithNullBody_returnsApiErrorEmptyBody() = runTest {
        val mockHeaders = Headers.Builder().build()
        val mockResponse: Response<String> = Response.success(null, mockHeaders)
        val apiCall: suspend () -> Response<String> = mockk()

        coEvery { apiCall.invoke() } returns mockResponse

        val result = safeApiCall(apiCall)

        assertThat(result, `is`(instanceOf(ResultData.ApiError::class.java)))
        val apiErrorResult = result as ResultData.ApiError
        assertThat(apiErrorResult.message, equalTo("Empty response body"))
        assertThat(apiErrorResult.code, equalTo(mockResponse.code()))
    }

    @Test
    fun safeApiCall_apiCallNotSuccessful_returnsApiErrorWithMessageAndCode() = runTest {
        val errorCode = 404
        val errorMessage = "Not Found"
        val mockResponse: Response<String> = Response.error(errorCode, errorMessage.toResponseBody())
        val apiCall: suspend () -> Response<String> = mockk()

        coEvery { apiCall.invoke() } returns mockResponse

        val result = safeApiCall(apiCall)

        assertThat(result, `is`(instanceOf(ResultData.ApiError::class.java)))
        val apiErrorResult = result as ResultData.ApiError

        assertThat(apiErrorResult.message, equalTo(errorMessage))
        assertThat(apiErrorResult.code, equalTo(errorCode))
    }

    @Test
    fun safeApiCall_apiCallThrowsIOException_returnsOperationErrorWithNoNetworkConnectionException() = runTest {
        val ioException = IOException("Network unreachable")
        val apiCall: suspend () -> Response<String> = mockk()

        coEvery { apiCall.invoke() } throws ioException

        val result = safeApiCall(apiCall)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationErrorResult = result as ResultData.OperationError
        assertThat(operationErrorResult.exception, `is`(instanceOf(NoNetworkConnectionException::class.java)))
    }

    @Test
    fun safeApiCall_apiCallThrowsOtherException_returnsOperationErrorWithUnknownException() = runTest {
        val otherException = Exception("Something went wrong")
        val apiCall: suspend () -> Response<String> = mockk()

        coEvery { apiCall.invoke() } throws otherException

        val result = safeApiCall(apiCall)

        assertThat(result, `is`(instanceOf(ResultData.OperationError::class.java)))
        val operationErrorResult = result as ResultData.OperationError
        assertThat(operationErrorResult.exception, `is`(instanceOf(UnknownException::class.java)))
    }
}