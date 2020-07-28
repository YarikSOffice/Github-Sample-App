package com.github.sample.data.common

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject

class HttpLogger : HttpLoggingInterceptor.Logger {

    private fun print(string: String) {
        Log.d(TAG, string)
    }

    override fun log(message: String) {
        val value = if (message.startsWith("{") || message.startsWith("[")) {
            try {
                JSONObject(message).toString(4)
            } catch (e: JSONException) {
                message
            }
        } else {
            message
        }
        print(value)
    }

    companion object {
        private const val TAG = "HTTP"
    }
}
