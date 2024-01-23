package ca.josue_lubaki.callapp.presentation.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.josue_lubaki.callapp.BuildConfig
import ca.josue_lubaki.callapp.ui.theme.CallAppTheme
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone

@Composable
fun CallScreen(viewModel : CallViewModel) {
//    val call by viewModel.call.collectAsState()
    val call = viewModel.call

    // Ask for permissions
    LaunchCallPermissions(call = call)

    LaunchedEffect(key1 = Unit) {
        call.join(create = true)
    }

//    val onStartCall = { viewModel.onEvent(CallEvent.OnStartCall) }
//    val onEndCall = { viewModel.onEvent(CallEvent.OnEndCall) }

    CallAppTheme {
        // Apply the theme to the call
        VideoTheme {
            val connection by call.state.connection.collectAsState()

            if (connection != RealtimeConnection.Connected) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "You can start call with ID : ${BuildConfig.STREAM_CALL_ID}",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )

//                        Button(onClick = { onStartCall() }) {
//                            Text(text = "Start Call")
//                        }
                    }
                }
            }
            else {
                CallContent(
                    modifier = Modifier.background(color = VideoTheme.colors.appBackground),
                    call = call,
                    onCallAction = { callAction ->
                        when (callAction) {
                            is FlipCamera -> call.camera.flip()
                            is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                            is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
//                            is LeaveCall -> onEndCall()
                            else -> Unit
                        }
                    }
                )
            }
        }
    }
}