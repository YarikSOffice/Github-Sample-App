package com.github.sample.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sample.domain.model.Repo
import com.github.sample.domain.repository.RepoRepository
import com.github.sample.ui.util.SingleEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Event {
    object Error : Event()
}

class HistoryViewModel @Inject constructor(
    private val repository: RepoRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Repo>>()
    val items: LiveData<List<Repo>> = _items

    private val _event = MutableLiveData<SingleEvent<Event>>()
    val event: LiveData<SingleEvent<Event>> = _event

    init {
        viewModelScope.launch {
            try {
                val data = repository.getRepositoryHistory()
                _items.value = data
            } catch (e: Throwable) {
                _event.value = SingleEvent(Event.Error)
            }
        }
    }
}
