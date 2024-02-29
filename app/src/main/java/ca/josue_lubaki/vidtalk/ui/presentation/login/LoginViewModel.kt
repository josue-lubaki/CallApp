package ca.josue_lubaki.vidtalk.ui.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.domain.models.User
import ca.josue_lubaki.vidtalk.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
    private val dispatchers: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            //==== if you need execute actions ====//
            is LoginEvent.OnLogin -> event.loginUser()
        }
    }

    private fun LoginEvent.OnLogin.loginUser() {
        _state.value = LoginState.Loading
        try {
            viewModelScope.launch(dispatchers) {
                //==== if it's not a flow ====//

                val user = User(
                    email = email,
                    password = password
                )

                when (val response = useCase(user)) {
                    is LoginStatus.Success -> {
                        _state.value = LoginState.Success
                    }
                    is LoginStatus.Error -> {
                        _state.value = LoginState.Error(Exception(response.error))
                    }
                }

            }
        } catch (e: Exception) {
            // handle errors
            _state.value = LoginState.Error(e)
        }
    }
}