package com.github.sample.data.auth

import com.github.sample.data.user.UserStore
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val userStore: UserStore
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val original = chain.request()
        val token = userStore.getToken()
        return if (token.isNotEmpty()) {
            val authorized =
                original.newBuilder()
                    .header(AUTHORIZATION_HEADER, TOKEN_TEMPLATE.format(token))
                    .build()
            chain.proceed(authorized)
        } else {
            chain.proceed(original)
        }
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val TOKEN_TEMPLATE = "token %s"
    }
}
