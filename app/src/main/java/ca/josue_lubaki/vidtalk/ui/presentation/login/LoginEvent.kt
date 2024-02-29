package ca.josue_lubaki.vidtalk.ui.presentation.login

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

sealed class LoginEvent {
    data class OnLogin(val email : String, val password : String) : LoginEvent()
}