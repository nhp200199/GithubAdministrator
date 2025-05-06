package com.phucnguyen.githubadministrator.common.utils

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

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
}