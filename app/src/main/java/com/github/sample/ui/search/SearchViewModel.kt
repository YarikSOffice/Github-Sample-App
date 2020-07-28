@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.github.sample.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.sample.domain.model.Repo
import com.github.sample.domain.repository.RepoRepository
import com.github.sample.ui.util.SingleEvent
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Event {
    object Error : Event()
}

class SearchViewModel @Inject constructor(
    private val repository: RepoRepository
) : ViewModel() {

    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    private val _items = MutableLiveData<PagingData<Repo>>()
    val items: LiveData<PagingData<Repo>> = _items

    private val _event = MutableLiveData<SingleEvent<Event>>()
    val event: LiveData<SingleEvent<Event>> = _event

    init {
        viewModelScope.launch {
            queryChannel.asFlow()
                .debounce(SEARCH_DEBOUNCE_DELAY)
                .flatMapLatest { createSearchFlow(it) }
                .catch { _event.value = SingleEvent(Event.Error) }
                .collect {
                    _items.value = it
                }
        }
    }

    private fun createSearchFlow(it: String): Flow<PagingData<Repo>> {
        return if (it.isNotEmpty()) {
            Pager(PagingConfig(DEFAULT_PAGE_SIZE)) {
                RepositoryPagingSource(
                    repository,
                    it
                )
            }.flow.cachedIn(viewModelScope)
        } else {
            flowOf(PagingData.empty())
        }
    }

    fun query(query: String) {
        Log.d("TAG", "Query: $query")
        viewModelScope.launch {
            queryChannel.send(query)
        }
    }

    fun recordVisitedRepo(repo: Repo) {
        viewModelScope.launch {
            repository.addToHistory(repo)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
        private const val DEFAULT_PAGE_SIZE = 30
    }
}
