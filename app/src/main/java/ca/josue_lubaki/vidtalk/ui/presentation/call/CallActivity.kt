
package ca.josue_lubaki.vidtalk.ui.presentation.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import ca.josue_lubaki.vidtalk.MainActivity
import io.getstream.result.Result
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.notifications.NotificationHandler
import io.getstream.video.android.model.StreamCallId
import io.getstream.video.android.model.streamCallId
import kotlinx.coroutines.launch

class CallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // step 1 - get the StreamVideo instance and create a call
        val streamVideo = StreamVideo.instance()
        val cid = intent.streamCallId(EXTRA_CID)
            ?: throw IllegalArgumentException("call type and id is invalid!")

        // optional - check for already active call that can be utilized
        val activeCall = streamVideo.state.activeCall.value
        val call = if (activeCall != null) {
            if (activeCall.id != cid.id) {
                Log.d("xxxx","A call with id: ${cid.cid} existed. Leaving.")
                // If the call id is different leave the previous call, Return a new call
                activeCall.leave()
                streamVideo.call(type = cid.type, id = cid.id)
            } else {
                // Call ID is the same, use the active call
                activeCall
            }
        } else {
            // There is no active call, create new call
            streamVideo.call(type = cid.type, id = cid.id)
        }

        call.camera.setEnabled(true)
        call.camera.flip()

        // step 2 - join a call
        lifecycleScope.launch {
            // If the call is new, join the call
            if (activeCall != call) {
                val result = call.join(create = true)

                // Unable to join. Device is offline or other usually connection issue.
                if (result is Result.Failure) {
                    Log.e("xxxx","CallActivity Call.join failed ${result.value}")
                    Toast.makeText(
                        this@CallActivity,
                        "Failed to join call (${result.value.message})",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }
            }
        }

        // step 3 - build a call screen
        setContent {
            val connection by call.state.connection.collectAsState()
            LaunchCallPermissions(call = call)

            LaunchedEffect(key1 = connection) {
                if (connection is RealtimeConnection.Disconnected) {
                    goBackToMainScreen()
                } else if (connection is RealtimeConnection.Failed) {
                    Toast.makeText(
                        this@CallActivity,
                        "Call connection failed (${(connection as RealtimeConnection.Failed).error}",
                        Toast.LENGTH_LONG,
                    ).show()
                    goBackToMainScreen()
                }
            }

            CallScreen(
                call = call,
                onCallDisconnected = {
                    // call state changed to disconnected - we can leave the screen
                    goBackToMainScreen()
                },
                onUserLeaveCall = {
                    call.leave()
                    // we don't need to wait for the call state to change to disconnected, we can
                    // leave immediately
                    goBackToMainScreen()
                },
            )
        }
    }

    private fun goBackToMainScreen() {
        if (!isFinishing) {
            val intent = Intent(this@CallActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
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
}
