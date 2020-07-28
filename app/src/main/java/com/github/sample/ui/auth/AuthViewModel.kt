package com.github.sample.ui.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sample.domain.auth.AuthInteractor
import com.github.sample.ui.auth.State.IDLE
import com.github.sample.ui.auth.State.LOADING
import com.github.sample.ui.util.SingleEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class State {
    LOADING, IDLE
}

sealed class Event {
    object Authorization : Event()
    object Error : Event()
}

class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    private val _state = MutableLiveData<State>(IDLE)
    val state: LiveData<State> = _state

    private val _event = MutableLiveData<SingleEvent<Event>>()
    val event: LiveData<SingleEvent<Event>> = _event

    fun generateAuthUri(): Uri {
        return interactor.generateAuthUri()
    }

    fun handleAuthRedirection(uri: Uri) {
        viewModelScope.launch {
            _state.value = LOADING
            try {
                interactor.authorize(uri)
                _event.value = SingleEvent(Event.Authorization)
            } catch (e: Throwable) {
                if (e is CancellationException) {
                    throw e
                } else {
                    _event.value = SingleEvent(Event.Error)
                    _state.value = IDLE
                }
            }
        }

    }
}
