package com.github.sample.domain.repository

import com.github.sample.domain.model.Repo
import com.github.sample.domain.model.RepositoryResponse

interface RepoRepository {

    suspend fun searchRepos(query: String, page: Int): RepositoryResponse

    suspend fun getRepositoryHistory(): List<Repo>

    suspend fun addToHistory(repo: Repo)
}
