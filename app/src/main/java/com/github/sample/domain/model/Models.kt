package com.github.sample.domain.model

data class Repo(
    override val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val owner: Owner,
    val stars: Int,
    val url: String
) : WithId

data class Owner(
    val login: String,
    val url: String?,
    val avatarUrl: String?
)

data class AuthResponse(
    val accessToken: String
)

data class Pagination(
    val totalPages: Int,
    val nextPage: Int?
)

data class RepositoryResponse(
    val items: List<Repo>,
    var pagination: Pagination
)

interface WithId {
    val id: Long
}
