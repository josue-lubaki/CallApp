package ca.josue_lubaki.vidtalk.ui.presentation.login

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ca.josue_lubaki.vidtalk.navigation.ScreenTarget

/**
 * created by Josue Lubaki
 * date : 2024-02-16
 * version : 1.0.0
 */

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRoute: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    val onLogin: (String, String) -> Unit = { email, password ->
        viewModel.onEvent(LoginEvent.OnLogin(email, password))
    }

    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = state) {
        when (state) {
            is LoginState.Success -> {
                onNavigateToRoute(ScreenTarget.CallJoin.route)
            }
            is LoginState.Error -> {
                error.value = (state as LoginState.Error).error.message
            }
            else -> Unit
        }
    }

    Content(
        state = state,
        onLogin = onLogin,
        error = error.value,
    )
}

@Composable
private fun Content(
    state: LoginState,
    onLogin: (String, String) -> Unit,
    error: String? = null
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
        ,
        contentAlignment = Alignment.Center
    ) {

        val email = rememberSaveable { mutableStateOf("josuelubaki@gmail.com") }
        val password = rememberSaveable { mutableStateOf("123456") }

        val isValid = remember(email.value, password.value) {
            email.value.isNotEmpty() && password.value.isNotEmpty()
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Forms(
                    modifier = Modifier,
                    email = email,
                    password = password,
                    isValid = isValid,
                    onLogin = onLogin
                )

                if (state is LoginState.Loading) {
                    CircularProgressIndicator()
                }

                if (state is LoginState.Error) {
                    Text(text = error ?: "An error occurred")
                }
            }
        }
    }
}

@Composable
fun Forms(
    modifier : Modifier = Modifier,
    email: MutableState<String>,
    password: MutableState<String>,
    isValid: Boolean,
    onLogin: (String, String) -> Unit
) {
    val emailValue = email.value
    val passwordValue = password.value

    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val passwordVisible = remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }

   Column(
       modifier = modifier,
       verticalArrangement = Arrangement.spacedBy(16.dp),
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       TextField(
           value = emailValue,
           onValueChange = { email.value = it },
           label = { Text("Email") },
       )
       if (emailError.value && interaction.collectIsPressedAsState().value) {
           Text(
               text = "Email is required",
               color = Color.Red
           )
       }

       TextField(
           value = passwordValue,
           onValueChange = { password.value = it },
           label = { Text("Password") },
           visualTransformation = PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }) {
                  if (passwordVisible.value) {
                      Icon(
                          imageVector = Icons.Filled.Visibility,
                          contentDescription = "Hide password"
                      )
                  } else {
                      Icon(
                          imageVector = Icons.Filled.VisibilityOff,
                          contentDescription = "Show password"
                      )
                  }
                }
              }
       )

       if (passwordError.value && interaction.collectIsPressedAsState().value) {
           Text(
               text = "Password is required",
               color = Color.Red
           )
       }

       Button(
           onClick = { onLogin(emailValue, passwordValue) },
           interactionSource = interaction,
           enabled = isValid
       ) {
          Text("LOGIN")
       }
   }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    Content(
        state = LoginState.Idle,
        onLogin = { _, _ -> },
        error = "An error occurred"
    )
}