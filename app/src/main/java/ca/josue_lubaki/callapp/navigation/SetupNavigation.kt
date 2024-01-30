package ca.josue_lubaki.callapp.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ca.josue_lubaki.callapp.BuildConfig
import ca.josue_lubaki.callapp.presentation.call.CallActivity
//import ca.josue_lubaki.callapp.presentation.call.CallScreen
import ca.josue_lubaki.callapp.presentation.chat.ChatScreen
import ca.josue_lubaki.callapp.presentation.home.HomeScreen
import ca.josue_lubaki.callapp.presentation.profile.ProfileScreen
import io.getstream.video.android.model.StreamCallId
import org.koin.androidx.compose.koinViewModel

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

@Composable
fun SetupNavigation(
    navController : NavHostController,
    onNavigateToRoute: (route: String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                items = listOf(
                    BottomItem.Home,
                    BottomItem.Call,
                    BottomItem.Chat,
                    BottomItem.Profile
                ),
                onNavigateToRoute = onNavigateToRoute
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ){
            NavHost(
                navController = navController,
                startDestination = ScreenTarget.Home.route
            ) {

                composable(ScreenTarget.Home.route) {
                    HomeScreen()
                }

                composable(ScreenTarget.Call.route) {
//                    CallScreen(viewModel = koinViewModel())
                    val context = LocalContext.current

                    val intent = CallActivity.createIntent(
                        context = context,
                        callId = StreamCallId.fromCallCid("default:${BuildConfig.STREAM_CALL_ID}"),
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)

//                    val intent= Intent(context, CallActivity::class.java)
//                    intent.putExtra("callId", "")
//                    context.startActivity(intent)
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

//                    CallActivity(
//                        viewModel = koinViewModel(),
//                        navigateToHome = {
//                            navController.navigate(ScreenTarget.Home.route)
//                        },
//                        navigateToChat = {
//                            navController.navigate(ScreenTarget.Chat.route)
//                        },
//                        navigateToProfile = {
//                            navController.navigate(ScreenTarget.Profile.route)
//                        },
//                    )
                }

                composable(ScreenTarget.Chat.route) {
                    ChatScreen(onNavigateToRoute = onNavigateToRoute)
                }

                composable(ScreenTarget.Profile.route) {
                    ProfileScreen(onNavigateToRoute = onNavigateToRoute)
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    items: List<BottomItem>,
    onNavigateToRoute: (route: String) -> Unit
) {
    NavigationBar() {
        items.forEach { screen ->
            NavigationBarItem(
                icon = screen.icon,
                label = screen.label,
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    onNavigateToRoute(screen.route)
//                    navController.navigate(screen.route) {
//                        this.launchSingleTop = true
//                        this.restoreState = false
//                        this.popUpTo(navController.graph.startDestinationId) {
//                            this.inclusive = true
//                        }
//                        navController.navigate(screen.route)
//                        popUpTo(navController.graph.startDestinationId) {
//                            inclusive = true
//                        }
//                    }
                }
            )
        }
    }
}
