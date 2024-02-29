package ca.josue_lubaki.vidtalk.di

import ca.josue_lubaki.vidtalk.data.datasourceimpl.LoginRemoteDataSourceImpl
import ca.josue_lubaki.vidtalk.data.datasource.LoginRemoteDataSource
import ca.josue_lubaki.vidtalk.data.repository.LoginRepositoryImpl
import ca.josue_lubaki.vidtalk.domain.repository.LoginRepository
import ca.josue_lubaki.vidtalk.domain.usecases.LoginUseCase
import ca.josue_lubaki.vidtalk.ui.presentation.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    internal fun provideLoginRemoteDataSource(dataStore : StreamUserDataStore) : LoginRemoteDataSource {
        return LoginRemoteDataSourceImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(remoteDataSource: LoginRemoteDataSource): LoginRepository {
        return LoginRepositoryImpl(remoteDataSource = remoteDataSource)
    }

    @Provides
    @Singleton
    internal fun provideLoginUseCase(repository : LoginRepository): LoginUseCase {
        return LoginUseCase(repository = repository)
    }

    @Provides
    fun provideLoginViewModelFactory(
        loginUseCase: LoginUseCase,
        dispatchers : CoroutineDispatcher,
    ): LoginViewModel {
        return LoginViewModel(
            useCase = loginUseCase,
            dispatchers = dispatchers,
        )
    }
}