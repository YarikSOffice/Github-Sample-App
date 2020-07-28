package com.github.sample.data.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryResponseEntity(
    val items: List<RepoEntity>
)

@JsonClass(generateAdapter = true)
data class RepoEntity(
    val id: Long,
    val name: String,
    @Json(name = "full_name")
    val fullName: String,
    val description: String?,
    val owner: OwnerEntity,
    @Json(name = "stargazers_count")
    val stars: Int,
    @Json(name = "html_url")
    val url: String
)

@JsonClass(generateAdapter = true)
data class OwnerEntity(
    val login: String,
    val url: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String?
)

@JsonClass(generateAdapter = true)
data class AuthResponseEntity(
    @Json(name = "access_token")
    val accessToken: String
)

@JsonClass(generateAdapter = true)
data class AuthBody(
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "client_secret")
    val clientSecret: String,
    @Json(name = "code")
    val code: String
)
