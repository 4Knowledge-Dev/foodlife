package com.forknowledge.feature.authentication.ui.screen.signInOptions

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.data.AuthenticationManager
import com.forknowledge.core.data.datatype.LoginResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResultType>(LoginResultType.NONE)
    val loginState: StateFlow<LoginResultType> = _loginState

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            authenticationManager
                .signInWithGoogleCredential(context)
                .collect { resultType ->
                    _loginState.update { resultType }
                }
        }
    }
}
