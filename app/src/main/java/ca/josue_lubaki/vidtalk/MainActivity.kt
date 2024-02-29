package ca.josue_lubaki.vidtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import ca.josue_lubaki.vidtalk.navigation.SetupNavGraph
import ca.josue_lubaki.vidtalk.ui.presentation.login.LoginScreen
import ca.josue_lubaki.vidtalk.ui.theme.VidTalkTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStore: StreamUserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLoggedIn = dataStore.user.firstOrNull() != null
            setContent {
                val navController = rememberNavController()
                VidTalkTheme {
//                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        VideoTheme {
                            SetupNavGraph(
                                isLoggedIn = isLoggedIn,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}