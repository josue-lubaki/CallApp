package ca.josue_lubaki.callapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import ca.josue_lubaki.callapp.navigation.SetupNavigation
import ca.josue_lubaki.callapp.ui.theme.CallAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CallAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navHostController = rememberNavController()
                    SetupNavigation(
                        navController = navHostController,
                        onNavigateToRoute = { route ->
                            navHostController.navigate(route){
                                launchSingleTop = true
//                                popUpTo(navHostController.graph.startDestinationId){
//                                    inclusive = true
//                                }
                            }
                        }
                    )
                }
            }
        }
    }
}