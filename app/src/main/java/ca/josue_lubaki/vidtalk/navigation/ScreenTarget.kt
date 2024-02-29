package ca.josue_lubaki.vidtalk.navigation

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

sealed class ScreenTarget(val route : String) {
    data object Login : ScreenTarget("login")
    data object CallJoin : ScreenTarget("call_join")
    data object CallLobby : ScreenTarget("call_lobby/{cid}")
    data object App : ScreenTarget("app")

    fun routeWithArg(argValue: Any): String = when (this) {
        CallLobby -> this.route.replace("{cid}", argValue.toString())
        else -> this.route
    }
}