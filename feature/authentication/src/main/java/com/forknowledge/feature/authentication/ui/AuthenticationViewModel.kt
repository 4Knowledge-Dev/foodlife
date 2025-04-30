package com.forknowledge.feature.authentication.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.domain.component.AuthenticationManager
import com.forknowledge.feature.authentication.extension.isValidEmail
import com.forknowledge.feature.authentication.extension.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val EMAIL_KEY = "email"
private const val PASSWORD_KEY = "password"

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authenticationManager: AuthenticationManager,
): ViewModel() {

    internal val email = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    internal val password = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    internal var shouldShowEmailError = mutableStateOf(false)
    internal var shouldShowSignUpPasswordError = mutableStateOf(false)
    internal var isRegisterButtonEnabled = mutableStateOf(false)
    internal var isLoginButtonEnabled = mutableStateOf(false)
    internal var isEmailValidated = mutableStateOf(false)

    internal var shouldShowLoading = mutableStateOf(false)
    internal var alertDialogError = mutableStateOf<String?>(null)

    fun signInWithAvailableCredentials(context: Context) {
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            authenticationManager.signInWithGoogleCredential(context).collect {
            }
        }
    }

    fun signInWithFacebook() {
        viewModelScope.launch {
            authenticationManager.signInWithFacebookCredential().collect {
                
            }
        }
    }

    fun updateEmail(email: String) {
        shouldShowEmailError.value = false
        isLoginButtonEnabled.value = email.isNotEmpty() && password.value.isNotEmpty()
        isRegisterButtonEnabled.value = email.isNotEmpty() && password.value.isNotEmpty()
        savedStateHandle[EMAIL_KEY] = email
    }

    fun updatePassword(password: String) {
        shouldShowSignUpPasswordError.value = false
        isLoginButtonEnabled.value = password.isNotEmpty() && email.value.isNotEmpty()
        isRegisterButtonEnabled.value = email.value.isNotEmpty() && password.isNotEmpty()
        savedStateHandle[PASSWORD_KEY] = password
    }

    fun onValidate(email: String) {
        shouldShowEmailError.value = !email.isValidEmail()
        isEmailValidated.value = email.isValidEmail()
    }

    fun signInWithEmail(email: String) {
        viewModelScope.launch {
            authenticationManager.signInWithEmail(email, password.value)
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {  }

                            is Result.Success -> {
                                shouldShowLoading.value = false
                            }

                            is Result.Error -> {
                                shouldShowLoading.value = false
                                alertDialogError.value = result.exception.message
                            }
                        }
                }
        }
    }

    private fun signUpWithEmail() {
        shouldShowLoading.value = true
        viewModelScope.launch {
            authenticationManager.signUpWithEmail(
                email.value,
                password.value
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {  /* Do nothing */  }

                    is Result.Success -> {
                        shouldShowLoading.value = false
                        createUserData()
                    }

                    is Result.Error -> {
                        shouldShowLoading.value = false
                        alertDialogError.value = result.exception.message
                    }
                }
            }
        }
    }

    private fun createUserData() {
        TODO("Not yet implemented")
    }

    fun validateUser() {
        shouldShowEmailError.value = !email.value.isValidEmail()
        shouldShowSignUpPasswordError.value = !password.value.isValidPassword()
        if (email.value.isValidEmail() && password.value.isValidPassword()) {
            signUpWithEmail()
        }
    }

    fun resetStates() {
        savedStateHandle[EMAIL_KEY] = ""
        savedStateHandle[PASSWORD_KEY] = ""
        isLoginButtonEnabled.value = false
        isRegisterButtonEnabled.value = false
    }
}
