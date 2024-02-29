package ca.josue_lubaki.vidtalk.utils.config

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ca.josue_lubaki.vidtalk.BuildConfig
import ca.josue_lubaki.vidtalk.data.service.GetAuthDataResponse
import io.getstream.log.Priority
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.LoggingLevel
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import io.getstream.video.android.model.ApiKey
import io.getstream.video.android.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

public enum class InitializedState {
    NOT_STARTED, RUNNING, FINISHED, FAILED
}

@SuppressLint("StaticFieldLeak")
object StreamVideoInitHelper {

    private var isInitialising = false
    private lateinit var context: Context
    private val _initState = MutableStateFlow(InitializedState.NOT_STARTED)
    public val initializedState: StateFlow<InitializedState> = _initState

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    suspend fun loadSdk(
        dataStore: StreamUserDataStore,
        useRandomUserAsFallback: Boolean = true,
    ) = AppConfig.load(context) {
        if (StreamVideo.isInstalled) {
            _initState.value = InitializedState.FINISHED
            Log.d("xxxx","[initStreamVideo] StreamVideo is already initialised.")
            return@load
        }

        if (isInitialising) {
            _initState.value = InitializedState.RUNNING
            Log.d("xxxx","[initStreamVideo] StreamVideo is already initialising")
            return@load
        }

        isInitialising = true
        _initState.value = InitializedState.RUNNING

        try {
            var loggedInUser = dataStore.data.firstOrNull()?.user

            if (loggedInUser == null && useRandomUserAsFallback) {
                val userId = UserHelper.generateRandomString()
                loggedInUser = User(
                    id = userId,
                    name = "Josue Lubaki",
                    role = "admin"
                )

                // Store the data (note that this datastore belongs to the client - it's not
                // used by the SDK directly in any way)
                dataStore.updateUser(loggedInUser)
            }

            if (loggedInUser != null) {
                initializeStreamVideo(
                    context = context,
                    apiKey = BuildConfig.STREAM_API_KEY,
                    user = loggedInUser,
                    token = BuildConfig.STREAM_TOKEN, // use this for production
//                    token = StreamVideo.devToken(loggedInUser.id), // use this for development
                    loggingLevel = LoggingLevel(priority = Priority.VERBOSE),
                )
            }

            dataStore.updateUser(loggedInUser)
            Log.d("xxxx", "StreamVideoInitHelper : Init successful.")
            _initState.value = InitializedState.FINISHED
        } catch (e: Exception) {
            _initState.value = InitializedState.FAILED
            Log.d("xxxx", "StreamVideoInitHelper : Init failed.")
        }

        isInitialising = false
    }

    private fun initializeStreamVideo(
        context: Context,
        apiKey: ApiKey,
        user: User,
        token: String,
        loggingLevel: LoggingLevel,
    ): StreamVideo {
        return StreamVideoBuilder(
            context = context,
            apiKey = apiKey,
            user = user,
            token = token,
            loggingLevel = loggingLevel,
            ensureSingleInstance = false,
            geo = GEO.GlobalEdgeNetwork
        ).build()
    }
}