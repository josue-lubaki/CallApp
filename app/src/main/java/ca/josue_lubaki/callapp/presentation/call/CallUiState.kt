package ca.josue_lubaki.callapp.presentation.call

/**
 * created by Josue Lubaki
 * date : 2024-01-23
 * version : 1.0.0
 */

sealed interface CallUiState {
    data object Nothing : CallUiState

    data class JoinCompleted(val callId: String) : CallUiState

    data object GoBackToLogin : CallUiState
}