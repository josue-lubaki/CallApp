package ca.josue_lubaki.vidtalk.data.datasource

import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.domain.models.User

interface LoginRemoteDataSource {
    suspend fun login(user: User) : LoginStatus
}