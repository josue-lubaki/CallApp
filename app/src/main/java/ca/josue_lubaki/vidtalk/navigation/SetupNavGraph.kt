package ca.josue_lubaki.vidtalk.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ca.josue_lubaki.vidtalk.ui.presentation.call.CallJoinScreen
import ca.josue_lubaki.vidtalk.ui.presentation.lobby.CallLobbyScreen
import ca.josue_lubaki.vidtalk.ui.presentation.login.LoginScreen
import io.getstream.video.android.compose.theme.VideoTheme

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

@Composable
fun SetupNavGraph(
    isLoggedIn : Boolean,
    navController : NavHostController
) {
    val initialDestination = if (isLoggedIn) {
        ScreenTarget.CallJoin.route
    } else {
        ScreenTarget.Login.route
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = initialDestination,
    ){
        composable(ScreenTarget.Login.route) {
            LoginScreen(
                onNavigateToRoute = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(ScreenTarget.CallJoin.route){
            CallJoinScreen(
                navigateUpToLogin = {
                    navController.navigate(ScreenTarget.Login.route) {
                        popUpTo(ScreenTarget.CallJoin.route) { inclusive = true }
                    }
                },
                navigateToDirectCallJoin = {
//                    navController.navigate(ScreenTarget.DirectCallJoin.route)
                },
                navigateToBarcodeScanner = {
//                    navController.navigate(ScreenTarget.BarcodeScanning.route)
                },
                navigateToCallLobby = { cid ->
                    navController.navigate(ScreenTarget.CallLobby.routeWithArg(cid))
                }
            )
        }

        composable(
            ScreenTarget.CallLobby.route,
            arguments = listOf(navArgument("cid") { type = NavType.StringType }),
        ){
            CallLobbyScreen {
                navController.navigate(ScreenTarget.Login.route) {
                    popUpTo(ScreenTarget.CallJoin.route) { inclusive = true }
                }
            }
        }

        composable(ScreenTarget.App.route){
//            AppScreen(rootNavController = navController)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "App Screen")
            }
        }
    }}