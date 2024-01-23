package ca.josue_lubaki.callapp

import android.app.Application
import ca.josue_lubaki.callapp.di.callModule
import ca.josue_lubaki.callapp.di.commonModule
import ca.josue_lubaki.callapp.di.streamModule
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

/**
 * created by Josue Lubaki
 * date : 2024-01-19
 * version : 1.0.0
 */

class CallApplication : Application() {
        override fun onCreate() {
            super.onCreate()

            startKoin {
                androidContext(this@CallApplication)
                modules(
                    streamModule,
                    callModule,
                    commonModule
                )
            }
        }
}