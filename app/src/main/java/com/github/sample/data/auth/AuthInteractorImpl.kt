package com.github.sample.data.auth

import android.net.Uri
import com.github.sample.data.common.AuthBody
import com.github.sample.data.user.UserStore
import com.github.sample.domain.auth.AuthInteractor
import com.github.sample.domain.model.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    private val userStore: UserStore,
    private val api: AuthApi
) : AuthInteractor {

    override fun generateAuthUri(): Uri {
        val uniqueHash = UUID.randomUUID().toString()
        userStore.setAuthUniqueHash(uniqueHash)
        return Uri.parse(GITHUB_AUTH_URL)
            .buildUpon()
            .appendQueryParameter(CLIENT_ID_PARAM, GITHUB_CLIENT_ID)
            .appendQueryParameter(STATE_PARAM, uniqueHash)
            .build()
    }

    override suspend fun authorize(uri: Uri): AuthResponse = withContext(Dispatchers.IO) {
        val uniqueHash = uri.getQueryParameter(STATE_PARAM)!!
        check(uniqueHash == userStore.getAuthUniqueHash()) { "Auth is not secured" }
        val code = uri.getQueryParameter(CODE_PARAM)!!
        val auth = api.auth(
            AuthBody(
                GITHUB_CLIENT_ID,
                GITHUB_CLIENT_SECRET,
                code
            )
        )
        userStore.setToken(auth.accessToken)
        return@withContext AuthResponse(auth.accessToken)
    }

    override fun isAuthorized(): Boolean {
        return userStore.getToken().isNotEmpty()
    }

    companion object {
        private const val GITHUB_AUTH_URL = "https://github.com/login/oauth/authorize"
        private const val CLIENT_ID_PARAM = "client_id"
        private const val STATE_PARAM = "state"
        private const val CODE_PARAM = "code"

        // TODO should not be stored in VCS in a real app
        private const val GITHUB_CLIENT_ID = "c8f78a44f56184798226"
        private const val GITHUB_CLIENT_SECRET = "261cd68cf689161fa098de1c00c6878729010532"
    }
}
