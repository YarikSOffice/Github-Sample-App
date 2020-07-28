package com.github.sample.ui

import android.content.Intent

interface IntentInterceptor {
    fun onNewIntent(intent: Intent)
}
