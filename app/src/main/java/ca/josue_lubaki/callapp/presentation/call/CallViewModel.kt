//package ca.josue_lubaki.callapp.presentation.call
//
//import android.content.Context
//import androidx.compose.runtime.collectAsState
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import ca.josue_lubaki.callapp.BuildConfig
//import io.getstream.video.android.core.Call
//import io.getstream.video.android.core.GEO
//import io.getstream.video.android.core.StreamVideo
//import io.getstream.video.android.core.StreamVideoBuilder
//import io.getstream.video.android.model.User
//import io.getstream.video.android.model.UserType
//import io.getstream.video.android.model.mapper.isValidCallId
//import io.getstream.video.android.model.mapper.toTypeAndId
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.util.UUID
//
///**
// * created by Josue Lubaki
// * date : 2024-01-22
// * version : 1.0.0
// */
//
//class CallViewModel(
//    private val dispatcher: CoroutineDispatcher,
//    context: Context,
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(CallUiState.Nothing)
//    val state: StateFlow<CallUiState> = _state.asStateFlow()
//
//    private val _userData = User(
//        id = UUID.randomUUID().toString(), // any string
//        role = "user",
//        type = UserType.Authenticated,
//        name = "Josue Lubaki", // name and image are used in the UI
//        image = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=2662&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
//        )
//
//    private val user : Flow<User?> = MutableStateFlow(_userData)
//
//    private val userToken = BuildConfig.STREAM_TOKEN
//    private val callId = BuildConfig.STREAM_CALL_ID
//
////    private val user = User(
////        id = UUID.randomUUID().toString(), // any string
////        role = "user",
////        type = UserType.Authenticated,
////        name = "Josue Lubaki", // name and image are used in the UI
////        image = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=2662&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
////    )
//
//    private fun joinCall(callId: String? = null): Call {
//        val streamVideo = StreamVideo.instance()
//        val newCallId = callId ?: "default:${UUID.randomUUID()}"
//        val (type, id) = if (newCallId.isValidCallId()) {
//            newCallId.toTypeAndId()
//        } else {
//            "default" to newCallId
//        }
//        return streamVideo.call(type = type, id = id)
//    }
//
//    fun logOut() {
//        viewModelScope.launch {
//            StreamVideo.instance().logOut()
//            delay(200)
//            StreamVideo.removeClient()
//        }
//    }
//
////    private val client = StreamVideoBuilder(
////      context = context,
////      apiKey = BuildConfig.STREAM_API_KEY, // demo API key
////      geo = GEO.GlobalEdgeNetwork,
////      user = user,
////      token = userToken,
////    ).build()
//
//    lateinit var call : Call
//
////    init {
////        val client = StreamVideoBuilder(
////            context = context,
////            apiKey = BuildConfig.STREAM_API_KEY, // demo API key
////            geo = GEO.GlobalEdgeNetwork,
////            user = _userData,
////            token = userToken,
////        ).build()
////
//////        val call  = client.call(type = "default", id = callId)
////
////        call  = client.call(type = "default", id = callId)
////
////        viewModelScope.launch(dispatcher) {
////            call.join(create = true)
////        }
////    }
//
//
////    private val _call = call
////    private val _call = MutableStateFlow(client.call(type = "default", id = callId))
////    val call : StateFlow<Call> = _call.asStateFlow()
//
////    fun onEvent(event: CallEvent) {
////        when (event) {
////            is CallEvent.OnStartCall -> startCall()
////            is CallEvent.OnEndCall -> endCall()
////        }
////    }
////
////    private fun startCall() {
////        viewModelScope.launch(dispatcher) {
////            _call.value.join(create = true)
////        }
////    }
////
////    private fun endCall() {
////        viewModelScope.launch(dispatcher) {
////            _call.value.end()
////        }
////    }
//}