package ca.josue_lubaki.callapp

import android.app.Application
//import ca.josue_lubaki.callapp.di.callModule
import ca.josue_lubaki.callapp.di.commonModule
import ca.josue_lubaki.callapp.di.streamModule
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import java.util.UUID

/**
 * created by Josue Lubaki
 * date : 2024-01-19
 * version : 1.0.0
 */

class CallApplication : Application() {
        override fun onCreate() {
            super.onCreate()

//            val userToken = BuildConfig.STREAM_TOKEN
//            val callId = BuildConfig.STREAM_CALL_ID
//            val user = User(
//                id = UUID.randomUUID().toString(), // any string
//                role = "user",
//                type = UserType.Authenticated,
//                name = "Josue Lubaki", // name and image are used in the UI
//                image = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=2662&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
//            )
//
//            StreamVideoBuilder(
//                context = this,
//                apiKey = BuildConfig.STREAM_API_KEY, // demo API key
//                geo = GEO.GlobalEdgeNetwork,
//                user = user,
//                token = userToken,
//                ensureSingleInstance = false,
//            ).build()

            startKoin {
                androidContext(this@CallApplication)
                modules(
                    streamModule,
//                    callModule,
                    commonModule
                )
            }
        }
}