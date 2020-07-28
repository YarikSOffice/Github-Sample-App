package com.github.sample.data.repository.pagination

import android.util.Log
import com.github.sample.domain.model.Pagination
import retrofit2.Response
import java.util.regex.Pattern
import javax.inject.Inject

class PaginationParser @Inject constructor() {

    fun parsePagination(response: Response<*>): Pagination {
        val header = response.headers()[LINK_HEADER].orEmpty()
        val map = extractLinks(header)
        val totalPages = parsePage(map[LAST_LINK].orEmpty()) ?: 0
        val nextPage = parsePage(map[NEXT_LINK].orEmpty())
        return Pagination(totalPages, nextPage)
    }

    private fun extractLinks(string: String): Map<String, String> {
        val links = mutableMapOf<String, String>()
        val matcher = LINK_PATTERN.matcher(string)
        while (matcher.find()) {
            val count = matcher.groupCount()
            if (count == 2) {
                links[matcher.group(2)!!] = matcher.group(1)!!
            }
        }
        return links
    }

    private fun parsePage(string: String): Int? {
        val matcher = PAGE_PATTERN.matcher(string)
        return if (matcher.find() && matcher.groupCount() == 1) {
            try {
                Integer.parseInt(matcher.group(1)!!)
            } catch (ex: NumberFormatException) {
                Log.e("TAG", "Cannot parse next page from $string", ex)
                null
            }
        } else {
            null
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"
        private const val LAST_LINK = "last"
        private const val LINK_HEADER = "link"
    }
}
