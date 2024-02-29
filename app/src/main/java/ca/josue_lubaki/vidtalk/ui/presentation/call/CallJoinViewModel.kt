@file:OptIn(ExperimentalCoroutinesApi::class)

package ca.josue_lubaki.vidtalk.ui.presentation.call

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.vidtalk.utils.NetworkMonitor
import ca.josue_lubaki.vidtalk.utils.config.StreamVideoInitHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import io.getstream.video.android.model.User
import io.getstream.video.android.model.mapper.isValidCallId
import io.getstream.video.android.model.mapper.toTypeAndId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CallJoinViewModel @Inject constructor(
    private val dataStore: StreamUserDataStore,
    networkMonitor: NetworkMonitor,
) : ViewModel() {
    val user: Flow<User?> = dataStore.user
    val isLoggedOut = dataStore.user.map { it == null }
    var autoLogInAfterLogOut = true
    val isNetworkAvailable = networkMonitor.isNetworkAvailable

    private val event: MutableSharedFlow<CallJoinEvent> = MutableSharedFlow()
    internal val uiState: SharedFlow<CallJoinUiState> = event
        .flatMapLatest { event ->
            when (event) {
                is CallJoinEvent.GoBackToLogin -> {
                    flowOf(CallJoinUiState.GoBackToLogin)
                }
                is CallJoinEvent.JoinCall -> {
                    val call = joinCall(event.callId)
                    flowOf(CallJoinUiState.JoinCompleted(callId = call.cid))
                }
                is CallJoinEvent.JoinCompleted -> flowOf(
                    CallJoinUiState.JoinCompleted(event.callId),
                )
                else -> flowOf(CallJoinUiState.Nothing)
            }
        }
        .shareIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        viewModelScope.launch {
            isNetworkAvailable.collect { isNetworkAvailable ->
                if (isNetworkAvailable && !StreamVideo.isInstalled) {
                    StreamVideoInitHelper.loadSdk(
                        dataStore = dataStore,
                    )
                }
            }
        }
    }

    fun handleUiEvent(event: CallJoinEvent) {
        viewModelScope.launch { this@CallJoinViewModel.event.emit(event) }
    }

    private fun joinCall(callId: String? = null): Call {
        val streamVideo = StreamVideo.instance()
        val randomID = UUID.randomUUID().toString()
        Log.d("xxxx", "joinCall ID : $randomID")
        val newCallId = callId ?: "default:$randomID"
        val (type, id) = if (newCallId.isValidCallId()) {
            newCallId.toTypeAndId()
        } else {
            "default" to newCallId
        }
        return streamVideo.call(type = type, id = id)
    }

    fun logOut() {
        viewModelScope.launch {
            dataStore.clear()
            StreamVideo.instance().logOut()
            delay(200)
            StreamVideo.removeClient()
        }
    }
}

sealed interface CallJoinUiState {
    data object Nothing : CallJoinUiState

    data class JoinCompleted(val callId: String) : CallJoinUiState

    data object GoBackToLogin : CallJoinUiState
}

sealed interface CallJoinEvent {
    data object Nothing : CallJoinEvent

    data class JoinCall(val callId: String? = null) : CallJoinEvent

    data class JoinCompleted(val callId: String) : CallJoinEvent

    data object GoBackToLogin : CallJoinEvent
}
