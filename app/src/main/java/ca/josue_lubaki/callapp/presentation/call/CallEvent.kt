package ca.josue_lubaki.callapp.presentation.call

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

sealed class CallEvent {
    data object OnStartCall : CallEvent()
    data object OnEndCall : CallEvent()
}