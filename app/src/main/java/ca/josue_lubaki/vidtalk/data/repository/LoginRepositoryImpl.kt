package ca.josue_lubaki.vidtalk.data.repository

import ca.josue_lubaki.vidtalk.data.datasource.LoginRemoteDataSource
import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.domain.models.User
import ca.josue_lubaki.vidtalk.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun login(user: User): LoginStatus {
        return remoteDataSource.login(user = user)
    }
}