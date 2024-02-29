package ca.josue_lubaki.vidtalk.domain.repository

import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.domain.models.User

interface LoginRepository {
    suspend fun login(user: User) : LoginStatus
}