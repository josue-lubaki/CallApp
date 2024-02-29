package ca.josue_lubaki.vidtalk.ui.presentation.login

import androidx.annotation.StringRes

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val error: Exception) : LoginState()
}