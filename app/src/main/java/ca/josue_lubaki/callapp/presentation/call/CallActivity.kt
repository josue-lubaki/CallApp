package ca.josue_lubaki.callapp.presentation.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ca.josue_lubaki.callapp.BuildConfig
import ca.josue_lubaki.callapp.MainActivity
import ca.josue_lubaki.callapp.ui.theme.CallAppTheme
import io.getstream.result.Result
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.StreamCallId
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.util.UUID

class CallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val userToken = BuildConfig.STREAM_TOKEN
//        val callId = BuildConfig.STREAM_CALL_ID
        val user = User(
            id = UUID.randomUUID().toString(), // any string
            role = "user",
            type = UserType.Authenticated,
            name = "Josue Lubaki", // name and image are used in the UI
            image = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=2662&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        )

        StreamVideoBuilder(
            context = this,
            apiKey = BuildConfig.STREAM_API_KEY, // demo API key
            geo = GEO.GlobalEdgeNetwork,
            user = user,
            token = userToken,
            ensureSingleInstance = false
        ).build()

        // step 1 - get the StreamVideo instance and create a call
        val streamVideo = StreamVideo.instance()
        val cid = intent.streamCallId(EXTRA_CID)
            ?: throw IllegalArgumentException("call type and id is invalid!")

        // optional - check for already active call that can be utilized
        // This step is optional and can be skipped
        val activeCall = streamVideo.state.activeCall.value
        val call = if (activeCall != null) {
            if (activeCall.id != cid.id) {
                Log.w("CallActivity", "A call with id: ${cid.cid} existed. Leaving.")
                // If the call id is different leave the previous call
                activeCall.leave()
                // Return a new call
                streamVideo.call(type = cid.type, id = cid.id)
            } else {
                // Call ID is the same, use the active call
                activeCall
            }
        } else {
            // There is no active call, create new call
            streamVideo.call(type = cid.type, id = cid.id)
        }

        lifecycleScope.launch {
            // If the call is new, join the call
            if (activeCall != call) {
                val result = call.join(create = true)

                // Unable to join. Device is offline or other usually connection issue.
                if (result is Result.Failure) {
                    Log.e("xxxx", "CallActivity : Call.join failed ${result.value}")
                    Toast.makeText(
                        this@CallActivity,
                        "Failed to join call (${result.value.message})",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }
            }
        }

        setContent {

            CallAppTheme {

                LaunchCallPermissions(call = call)

                val onLeave = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        call.leave()
                        finish()

                        val intent = Intent(this@CallActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                }

                // Apply the theme to the call
                VideoTheme {
                    val connection by call.state.connection.collectAsState()

                    LaunchedEffect(key1 = connection) {
                        if (connection == RealtimeConnection.Disconnected) {
                            onLeave()
                        } else if (connection is RealtimeConnection.Failed) {
                            Toast.makeText(
                                this@CallActivity,
                                "Call connection failed (${(connection as RealtimeConnection.Failed).error}",
                                Toast.LENGTH_LONG,
                            ).show()
                            finish()
                        }
                    }

                    CallContent(
                        modifier = Modifier.background(color = VideoTheme.colors.appBackground),
                        call = call,
                        onCallAction = { callAction ->
                            when (callAction) {
                                is FlipCamera -> call.camera.flip()
                                is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                                is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                                is LeaveCall -> onLeave()
                                else -> Unit
                            }
                        }
                    )

                }
            }
        }
    }

    companion object {
        const val EXTRA_CID: String = NotificationHandler.INTENT_EXTRA_CALL_CID

        /**
         * @param callId the Call ID you want to join
         * and disable the microphone.
         */
        @JvmStatic
        fun createIntent(
            context: Context,
            callId: StreamCallId,
        ): Intent {
            return Intent(context, CallActivity::class.java).apply {
                putExtra(EXTRA_CID, callId)
            }
        }
    }

//    val onStartCall = { viewModel.onEvent(CallEvent.OnStartCall) }
//    val onEndCall = { viewModel.onEvent(CallEvent.OnEndCall) }
}