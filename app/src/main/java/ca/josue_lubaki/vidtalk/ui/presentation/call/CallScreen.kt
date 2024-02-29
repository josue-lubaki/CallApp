
package ca.josue_lubaki.vidtalk.ui.presentation.call

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.renderer.RegularVideoRendererStyle
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.mock.StreamPreviewDataUtils
import io.getstream.video.android.mock.previewCall

@Composable
fun CallScreen(
    call: Call,
    onCallDisconnected: () -> Unit = {},
    onUserLeaveCall: () -> Unit = {},
) {
    val context = LocalContext.current
    val speakingWhileMuted by call.state.speakingWhileMuted.collectAsStateWithLifecycle()

    val connection by call.state.connection.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = connection) {
        if (connection == RealtimeConnection.Disconnected) {
            onCallDisconnected.invoke()
        } else if (connection is RealtimeConnection.Failed) {
            Toast.makeText(
                context,
                "Call connection failed (${(connection as RealtimeConnection.Failed).error}",
                Toast.LENGTH_LONG,
            ).show()
            onCallDisconnected.invoke()
        }
    }

    VideoTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CallContent(
                modifier = Modifier.background(color = VideoTheme.colors.appBackground),
                call = call,
                onCallAction = { callAction ->
                    when (callAction) {
                        is FlipCamera -> call.camera.flip()
                        is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                        is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                        is LeaveCall -> onUserLeaveCall()
                        else -> Unit
                    }
                },
                onBackPressed = {
                    onUserLeaveCall.invoke()
                },
                style = RegularVideoRendererStyle(reactionDuration = 1650, reactionPosition = Alignment.Center),
            )
        }

        if (speakingWhileMuted) {
            SpeakingWhileMuted()
        }
    }
}

@Composable
private fun SpeakingWhileMuted() {
    Snackbar {
        Text(text = "You're talking while muting the microphone!")
    }
}

@Preview
@Composable
private fun CallScreenPreview() {
    StreamPreviewDataUtils.initializeStreamVideo(LocalContext.current)
    VideoTheme {
        CallScreen(call = previewCall)
    }
}
