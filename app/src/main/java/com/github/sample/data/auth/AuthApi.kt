package com.github.sample.data.auth

import com.github.sample.data.common.AuthBody
import com.github.sample.data.common.AuthResponseEntity
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @POST("login/oauth/access_token")
    @Headers(ACCEPT_HEADER)
    suspend fun auth(@Body body: AuthBody): AuthResponseEntity

    companion object {
        private const val ACCEPT_HEADER = "Accept: application/json"
    }
}
