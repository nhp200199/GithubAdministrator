package com.phucnguyen.githubadministrator.common.utils

fun extractNextSinceParameter(linkHeader: String?): Int? {
    if (linkHeader == null) return null

    val links = linkHeader.split(",")

    for (link in links) {
        val parts = link.split(";")
        if (parts.size >= 2) {
            val urlPart = parts[0].trim()
            val relPart = parts[1].trim()

            // Check if the relation is "next"
            if (relPart.contains("""rel="next"""")) {
                // Extract the URL from the angle brackets
                val url = urlPart.substringAfter("<").substringBefore(">")

                // Extract the 'since' parameter from the URL
                val sinceMatch = Regex("""since=(\d+)""").find(url)
                return sinceMatch?.groupValues?.get(1)?.toIntOrNull()
            }
        }
    }
    // Return null if the "next" link or "since" parameter is not found
    return null
}