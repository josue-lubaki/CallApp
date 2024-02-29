package ca.josue_lubaki.vidtalk.domain.usecases

import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.domain.models.User
import ca.josue_lubaki.vidtalk.domain.repository.LoginRepository

class LoginUseCase(
    private val repository: LoginRepository
) {

    suspend operator fun invoke(user : User) : LoginStatus = repository.login(user = user)
}