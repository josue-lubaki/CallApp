package ca.josue_lubaki.vidtalk.utils.config

import android.content.Context
import android.content.SharedPreferences
import ca.josue_lubaki.vidtalk.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * created by Josue Lubaki
 * date : 2024-01-25
 * version : 1.0.0
 */

object AppConfig {
    private const val SHARED_PREF_NAME = "stream_demo_app"

    private lateinit var prefs: SharedPreferences

    @OptIn(DelicateCoroutinesApi::class)
    fun load(
        context: Context,
        coroutineScope: CoroutineScope = GlobalScope,
        onLoaded: suspend () -> Unit = {},
    ) {
        // Load prefs
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        coroutineScope.launch {
            onLoaded()
        }
    }

}