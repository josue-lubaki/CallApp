package ca.josue_lubaki.vidtalk

import android.app.Application
import android.content.Context
import ca.josue_lubaki.vidtalk.utils.config.StreamVideoInitHelper
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import kotlinx.coroutines.runBlocking

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

@HiltAndroidApp
class VidTalkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        StreamUserDataStore.install(this, isEncrypted = true)

        // Demo helper for initialising the Video and Chat SDK instances from one place.
        // For simpler code we "inject" the Context manually instead of using DI.
        StreamVideoInitHelper.init(this)

        // Prepare the Video SDK if we already have a user logged in the demo app.
        // If you need to receive push messages (incoming call) then the SDK must be initialised
        // in Application.onCreate. Otherwise it doesn't know how to init itself when push arrives
        // and will ignore the push messages.
        // If push messages are not used then you don't need to init here - you can init
        // on-demand (initialising here is usually less error-prone).
        runBlocking {
            StreamVideoInitHelper.loadSdk(
                dataStore = StreamUserDataStore.instance(),
                useRandomUserAsFallback = false,
            )
        }
    }
}

val Context.app get() = applicationContext as VidTalkApplication