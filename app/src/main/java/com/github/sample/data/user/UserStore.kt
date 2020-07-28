package com.github.sample.data.user

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class UserStore @Inject constructor(
    context: Context
) {

    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)

    fun getToken(): String {
        return prefs.getString(TOKEN_KEY, "").orEmpty()
    }

    fun setToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthUniqueHash(): String {
        return prefs.getString(UNIQUE_HASH_KEY, "").orEmpty()
    }

    fun setAuthUniqueHash(hash: String) {
        prefs.edit().putString(UNIQUE_HASH_KEY, hash).apply()
    }

    companion object {
        private const val PREFERENCE_NAME = "user_prefs"
        private const val UNIQUE_HASH_KEY = "unique_hash"
        private const val TOKEN_KEY = "token"
    }
}
