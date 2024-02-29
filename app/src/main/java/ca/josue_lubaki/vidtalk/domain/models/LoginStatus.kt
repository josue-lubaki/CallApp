package ca.josue_lubaki.vidtalk.domain.models

sealed class LoginStatus {
    data object Success : LoginStatus()
    data class Error(val error: String) : LoginStatus()
}