package com.github.sample.data.repository

import com.github.sample.data.repository.pagination.PaginationParser
import com.github.sample.domain.model.Repo
import com.github.sample.domain.model.RepositoryResponse
import com.github.sample.domain.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val paginationParser: PaginationParser,
    private val mapper: RepositoryMapper,
    private val api: SearchApi,
    private val historyStore: RepoHistoryStore
) : RepoRepository {

    @Throws(IOException::class)
    override suspend fun searchRepos(
        query: String,
        page: Int
    ): RepositoryResponse = withContext(Dispatchers.IO) {
        val request1 = async { api.searchRepos(query, page) }
        // it's safe to request a page that is bigger than total pages available.
        // such response contains an empty list + the next page link is null
        val request2 = async { api.searchRepos(query, page + 1) }
        val response1 = request1.await()
        val response2 = request2.await()
        if (response1.isSuccessful && response2.isSuccessful) {
            val firstList = response1.body()!!.items
            val secondList = response2.body()!!.items
            val list = (firstList + secondList).map { mapper.mapTo(it) }
            val pagination = paginationParser.parsePagination(response2)
            return@withContext RepositoryResponse(list, pagination)
        } else {
            throw IOException("Can't load repositories")
        }
    }

    override suspend fun getRepositoryHistory(): List<Repo> = withContext(Dispatchers.IO) {
        return@withContext historyStore.getRepoHistory()
    }

    override suspend fun addToHistory(repo: Repo) = withContext(Dispatchers.IO) {
        historyStore.addToRepoHistory(repo)
    }
}
