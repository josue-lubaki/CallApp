@file:OptIn(ExperimentalComposeUiApi::class)

package ca.josue_lubaki.vidtalk.ui.presentation.call

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ca.josue_lubaki.vidtalk.BuildConfig
import ca.josue_lubaki.vidtalk.R
import ca.josue_lubaki.vidtalk.ui.theme.StreamButton
import ca.josue_lubaki.vidtalk.utils.NetworkMonitor
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.avatar.UserAvatar
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import io.getstream.video.android.mock.StreamPreviewDataUtils
import io.getstream.video.android.mock.previewUsers
import io.getstream.video.android.model.User

@Composable
fun CallJoinScreen(
    callJoinViewModel: CallJoinViewModel = hiltViewModel(),
    navigateToCallLobby: (callId: String) -> Unit,
    navigateUpToLogin: (autoLogIn: Boolean) -> Unit,
    navigateToDirectCallJoin: () -> Unit,
    navigateToBarcodeScanner: () -> Unit = {},
) {
    val uiState by callJoinViewModel.uiState.collectAsState(CallJoinUiState.Nothing)
    val user by callJoinViewModel.user.collectAsState(initial = null)

    var isSignOutDialogVisible by remember { mutableStateOf(false) }
    val isLoggedOut by callJoinViewModel.isLoggedOut.collectAsState(initial = false)
    val isNetworkAvailable by callJoinViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    HandleCallJoinUiState(
        callJoinUiState = uiState,
        navigateToCallLobby = navigateToCallLobby,
        navigateUpToLogin = { navigateUpToLogin(true) },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CallJoinHeader(
            user = user,
            onAvatarLongClick = { if (isNetworkAvailable) isSignOutDialogVisible = true },
            onDirectCallClick = navigateToDirectCallJoin,
            onSignOutClick = {
                callJoinViewModel.autoLogInAfterLogOut = false
                callJoinViewModel.logOut()
                navigateUpToLogin(true)
            },
        )

        CallJoinBody(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(0.dp, 500.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f),
            callJoinViewModel = callJoinViewModel,
            openCamera = {
                navigateToBarcodeScanner()
            },
            isNetworkAvailable = isNetworkAvailable,
        )
    }

    if (isSignOutDialogVisible) {
        SignOutDialog(
            onConfirmation = {
                isSignOutDialogVisible = false
                callJoinViewModel.autoLogInAfterLogOut = false
                callJoinViewModel.logOut()
                navigateUpToLogin(true)
            },
            onDismissRequest = { isSignOutDialogVisible = false },
        )
    }

    LaunchedEffect(key1 = isLoggedOut) {
        if (isLoggedOut) {
            navigateUpToLogin.invoke(callJoinViewModel.autoLogInAfterLogOut)
        }
    }
}

@Composable
private fun HandleCallJoinUiState(
    callJoinUiState: CallJoinUiState,
    navigateToCallLobby: (callId: String) -> Unit,
    navigateUpToLogin: () -> Unit,
) {
    LaunchedEffect(key1 = callJoinUiState) {
        when (callJoinUiState) {
            is CallJoinUiState.JoinCompleted ->
                navigateToCallLobby.invoke(callJoinUiState.callId)

            is CallJoinUiState.GoBackToLogin ->
                navigateUpToLogin.invoke()

            else -> Unit
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CallJoinHeader(
    user: User?,
    onAvatarLongClick: () -> Unit,
    onDirectCallClick: () -> Unit,
    onSignOutClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        user?.let {
            Box(
                modifier= Modifier.combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {},
                    onLongClick = onAvatarLongClick
                )
            ) {
                UserAvatar(
                    modifier = Modifier.size(48.dp),
                    userName = it.userNameOrId,
                    userImage = it.image,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            modifier = Modifier.weight(1f),
            color = Color.White,
            text = user?.name?.ifBlank { user.id }?.ifBlank { user.custom["email"] }.orEmpty(),
            maxLines = 1,
            fontSize = 16.sp,
        )

        if (user?.custom?.get("email")?.contains("getstreamio") == true) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
                content = { Text(text = stringResource(R.string.direct_call)) },
                onClick = { onDirectCallClick.invoke() },
            )
        }

            Spacer(modifier = Modifier.width(5.dp))

            StreamButton(
                modifier = Modifier.widthIn(125.dp),
                text = stringResource(id = R.string.sign_out),
                onClick = onSignOutClick,
            )
    }
}

@Composable
private fun CallJoinBody(
    modifier: Modifier,
    openCamera: () -> Unit,
    callJoinViewModel: CallJoinViewModel = hiltViewModel(),
    isNetworkAvailable: Boolean,
) {
    val previewUsers = listOf(
        User(
            id = "thierry",
            name = "Thierry",
            image = "https://avatars.githubusercontent.com/u/265409?v=4",
            role = "admin",
        ),
        User(
            id = "jaewoong",
            name = "Jaewoong Eum",
            image = "https://ca.slack-edge.com/T02RM6X6B-U02HU1XR9LM-626fb91c334e-128",
            role = "admin",
        )
    )

    val user by if (LocalInspectionMode.current) {
        remember { mutableStateOf(previewUsers[0]) }
    } else {
        callJoinViewModel.user.collectAsState(initial = null)
    }

    if (!isNetworkAvailable) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            StreamLogo()

            Spacer(modifier = Modifier.height(25.dp))

            AppName()

            Spacer(modifier = Modifier.height(25.dp))

            Description(text = stringResource(id = R.string.you_are_offline))
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .semantics { testTagsAsResourceId = true },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (user != null) {
                StreamLogo()

                Spacer(modifier = Modifier.height(25.dp))

                AppName()

                Spacer(modifier = Modifier.height(20.dp))

                Description(text = stringResource(id = R.string.join_description))

                Spacer(modifier = Modifier.height(42.dp))

                Label(text = stringResource(id = R.string.call_id_number))

                Spacer(modifier = Modifier.height(8.dp))

                JoinCallForm(openCamera = openCamera, callJoinViewModel = callJoinViewModel)

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.join_call_no_id_hint))

                Spacer(modifier = Modifier.height(8.dp))

                StreamButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 35.dp)
                        .testTag("start_new_call"),
                    text = stringResource(id = R.string.start_a_new_call),
                    onClick = { callJoinViewModel.handleUiEvent(CallJoinEvent.JoinCall()) },
                )
            }
        }
    }
}

@Composable
private fun StreamLogo() {
    Image(
        modifier = Modifier.size(102.dp),
        painter = painterResource(id = R.drawable.ic_stream_video_meeting_logo),
        contentDescription = null,
    )
}

@Composable
private fun AppName() {
    Text(
        modifier = Modifier.padding(horizontal = 30.dp),
        text = stringResource(id = R.string.app_name),
        color = Color.White,
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun Description(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        modifier = Modifier.widthIn(0.dp, 320.dp),
    )
}

@Composable
private fun Label(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp),
        text = text,
        color = Color(0xFF979797),
        fontSize = 13.sp,
    )
}

@Composable
private fun JoinCallForm(
    openCamera: () -> Unit,
    callJoinViewModel: CallJoinViewModel,
) {
    val callId by remember {
        mutableStateOf("default:${BuildConfig.STREAM_CALL_ID}")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 35.dp),
    ) {
        StreamButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxHeight()
                .testTag("join_call"),
            onClick = {
                callJoinViewModel.handleUiEvent(CallJoinEvent.JoinCall(callId = callId))
            },
            text = stringResource(id = R.string.join_call),
        )
    }
}

@Composable
private fun SignOutDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.background),
            RoundedCornerShape(6.dp),
        ),
        title = { Text(text = stringResource(id = R.string.sign_out)) },
        text = { Text(text = stringResource(R.string.are_you_sure_sign_out)) },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    text = stringResource(id = R.string.sign_out),
                    color = VideoTheme.colors.primaryAccent,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = VideoTheme.colors.primaryAccent,
                )
            }
        },
        onDismissRequest = { onDismissRequest() },
        shape = RoundedCornerShape(6.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        textContentColor = Color.White,
    )
}

@Preview
@Composable
private fun CallJoinScreenPreview() {
    StreamPreviewDataUtils.initializeStreamVideo(LocalContext.current)
    VideoTheme {
        StreamUserDataStore.install(LocalContext.current)
        CallJoinScreen(
            callJoinViewModel = CallJoinViewModel(
                dataStore = StreamUserDataStore.instance(),
                networkMonitor = NetworkMonitor(LocalContext.current),
            ),
            navigateToCallLobby = {},
            navigateUpToLogin = {},
            navigateToDirectCallJoin = {},
        )
    }
}
