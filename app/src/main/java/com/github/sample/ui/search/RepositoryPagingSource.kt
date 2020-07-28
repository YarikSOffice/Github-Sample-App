package com.github.sample.ui.search

import android.util.Log
import androidx.paging.PagingSource
import com.github.sample.domain.repository.RepoRepository
import com.github.sample.domain.model.Repo
import java.util.concurrent.CancellationException

class RepositoryPagingSource(
    private val repository: RepoRepository,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Repo> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = repository.searchRepos(query, nextPageNumber)
            Log.d("TAG", "Loaded count: ${response.items.size}")
            LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = response.pagination.nextPage
            )
        } catch (e: Throwable) {
            if (e is CancellationException) {
                throw e
            } else {
                LoadResult.Error(e)
            }
        }
    }
}
