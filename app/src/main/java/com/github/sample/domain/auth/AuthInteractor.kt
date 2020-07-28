package com.github.sample.domain.auth

import android.net.Uri
import com.github.sample.domain.model.AuthResponse

interface AuthInteractor {

    fun generateAuthUri(): Uri

    suspend fun authorize(uri: Uri): AuthResponse

    fun isAuthorized(): Boolean
}
