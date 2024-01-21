package ca.josue_lubaki.callapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ca.josue_lubaki.callapp.ui.theme.CallAppTheme
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.compose.ui.components.call.renderer.FloatingParticipantVideo
import io.getstream.video.android.compose.ui.components.call.renderer.ParticipantVideo
import io.getstream.video.android.compose.ui.components.video.VideoRenderer
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = BuildConfig.STREAM_USER_ID
        val userToken = BuildConfig.STREAM_TOKEN
        val callId = BuildConfig.STREAM_CALL_ID

        // step1 - create a user.
        val user = User(
            id = userId, // any string
            role = "user",
            type = UserType.Authenticated,
            name = "Josue Lubaki", // name and image are used in the UI
            image = "https://images.unsplassh.com/photo-1507003211169-0a1dd7228f2d?q=80&w=3087&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        )

        // step2 - initialize StreamVideo. For a production app we recommend adding the client to your Application class or di module.
        val client = StreamVideoBuilder(
            context = applicationContext,
            apiKey = BuildConfig.STREAM_API_KEY, // demo API key
            geo = GEO.GlobalEdgeNetwork,
            user = user,
            token = userToken,
        ).build()

        // step3 - join a call, which type is `default` and id is `123`.
        val call = client.call(type = "default", id = callId)
        lifecycleScope.launch {
            val result = call.join(create = true)
            result.onError {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            // step4 - request permissions.
            LaunchCallPermissions(call = call)

            CallAppTheme {
                // step5 - apply VideTheme
                VideoTheme {

                    val isCameraEnabled by call.camera.isEnabled.collectAsState()
                    val isMicrophoneEnabled by call.microphone.isEnabled.collectAsState()
                    val connection by call.state.connection.collectAsState()

                    if (connection != RealtimeConnection.Connected) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Connecting...",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    else {
                        CallContent(
                            modifier = Modifier.background(color = VideoTheme.colors.appBackground),
                            call = call,
                            onBackPressed = { onBackPressed() },
                            controlsContent = {
                                ControlActions(
                                    call = call,
                                    actions = listOf(
                                        {
                                            ToggleCameraAction(
                                                modifier = Modifier.size(52.dp),
                                                isCameraEnabled = isCameraEnabled,
                                                onCallAction = { call.camera.setEnabled(it.isEnabled) }
                                            )
                                        },
                                        {
                                            ToggleMicrophoneAction(
                                                modifier = Modifier.size(52.dp),
                                                isMicrophoneEnabled = isMicrophoneEnabled,
                                                onCallAction = { call.microphone.setEnabled(it.isEnabled) }
                                            )
                                        },
                                        {
                                            FlipCameraAction(
                                                modifier = Modifier.size(52.dp),
                                                onCallAction = { call.camera.flip() }
                                            )
                                        },
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}