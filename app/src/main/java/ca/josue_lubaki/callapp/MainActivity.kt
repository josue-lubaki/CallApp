package ca.josue_lubaki.callapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import io.getstream.video.android.compose.ui.components.call.renderer.FloatingParticipantVideo
import io.getstream.video.android.compose.ui.components.video.VideoRenderer
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.model.User
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
            name = "Tutorial", // name and image are used in the UI
            role = "admin",
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
            call.join(create = true)
        }

        setContent {
            CallAppTheme {
                // step4 - apply VideTheme
                VideoTheme {

//                    // step5 - render videos
                    CallContent(
                        modifier = Modifier.fillMaxSize(),
                        call = call,
                        enableInPictureInPicture = true,
                        onBackPressed = { finish() },
                        onCallAction = { callAction ->
                            when (callAction) {
                                is FlipCamera -> call.camera.flip()
                                is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                                is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                                is LeaveCall -> finish()
                                else -> Unit
                            }
                        },
                    )

                    // step5 - define required properties. Floating Video
//                    val remoteParticipants by call.state.remoteParticipants.collectAsState()
//                    val remoteParticipant = remoteParticipants.firstOrNull()
//                    val me by call.state.me.collectAsState()
//                    val connection by call.state.connection.collectAsState()
//                    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
//
//                    // step6 - request permissions.
//                    LaunchCallPermissions(call = call)
//
//                    // step7 - render a local and remote videos.
//                    Box(
//                        contentAlignment = Alignment.Center,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(VideoTheme.colors.appBackground)
//                            .onSizeChanged { parentSize = it },
//                    ) {
//                        if (remoteParticipant != null) {
//                            val remoteVideo by remoteParticipant.video.collectAsState()
//
//                            Column(modifier = Modifier.fillMaxSize()) {
//                                VideoRenderer(
//                                    modifier = Modifier.weight(1f),
//                                    call = call,
//                                    video = remoteVideo,
//                                )
//                            }
//                        } else {
//                            if (connection != RealtimeConnection.Connected) {
//                                Text(
//                                    text = "loading...",
//                                    fontSize = 30.sp,
//                                    color = VideoTheme.colors.textHighEmphasis,
//                                )
//                            } else {
//                                Text(
//                                    modifier = Modifier.padding(30.dp),
//                                    text = "Join call ${call.id} in your browser",
//                                    fontSize = 30.sp,
//                                    color = VideoTheme.colors.textHighEmphasis,
//                                    textAlign = TextAlign.Center,
//                                )
//                            }
//                        }
//
//                        // floating video UI for the local video participant
//                        if (me != null) {
//                            FloatingParticipantVideo(
//                                modifier = Modifier.align(Alignment.TopEnd),
//                                call = call,
//                                participant = me!!,
//                                parentBounds = parentSize,
//                            )
//                        }
//                    }
                }
            }
        }
    }
}