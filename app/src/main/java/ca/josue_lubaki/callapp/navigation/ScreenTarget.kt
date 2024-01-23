package ca.josue_lubaki.callapp.navigation

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

sealed class ScreenTarget(val route : String) {
    data object Home : ScreenTarget("home")
    data object Call : ScreenTarget("call")
    data object Chat : ScreenTarget("chat")
    data object Profile : ScreenTarget("profile")
}