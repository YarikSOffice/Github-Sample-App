package com.github.sample.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.github.sample.data.common.RepoEntity
import com.github.sample.domain.model.Repo
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * For the sake of simplicity, we utilize simple preferences + json here even though
 * the performance is pretty bad.
 */
class RepoHistoryStore @Inject constructor(
    context: Context,
    moshi: Moshi,
    private val mapper: RepositoryMapper
) {
    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
    private var adapter: JsonAdapter<List<RepoEntity>>

    init {
        val type: Type = Types.newParameterizedType(
            List::class.java,
            RepoEntity::class.java
        )
        adapter = moshi.adapter<List<RepoEntity>>(type)
    }

    fun getRepoHistory(): List<Repo> {
        return getCurrentList().map { mapper.mapTo(it) }
    }

    fun addToRepoHistory(repo: Repo) {
        val current = getCurrentList()
        val updated = current.toMutableList().apply {
            add(0, mapper.mapFrom(repo)) // bad performance
        }.take(HISTORY_CAPACITY)
        val json = adapter.toJson(updated)
        prefs.edit().putString(HISTORY_KEY, json).apply()
    }

    private fun getCurrentList(): List<RepoEntity> {
        return try {
            val value = prefs.getString(HISTORY_KEY, "").orEmpty()
            adapter.fromJson(value).orEmpty()
        } catch (e: Throwable) {
            emptyList()
        }
    }

    companion object {
        private const val HISTORY_CAPACITY = 20
        private const val PREFERENCE_NAME = "repo_history"
        private const val HISTORY_KEY = "history_key"
    }
}
