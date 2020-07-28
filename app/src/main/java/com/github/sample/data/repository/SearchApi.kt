package com.github.sample.data.repository

import com.github.sample.data.common.RepositoryResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/repositories?sort=stars&per_page=15")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Response<RepositoryResponseEntity>
}
