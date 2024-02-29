package ca.josue_lubaki.vidtalk.di

import android.content.Context
import ca.josue_lubaki.vidtalk.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    internal fun provideDispatchers() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideUserDataStore(): StreamUserDataStore {
        return StreamUserDataStore.instance()
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context) =
        NetworkMonitor(context)

}