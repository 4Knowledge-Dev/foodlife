package com.forknowledge.core.domain.component

import android.content.Context
import com.forknowledge.core.common.Result
import com.forknowledge.core.domain.LoginResultType
import kotlinx.coroutines.flow.Flow

interface AuthenticationManager {

    /**
     * Sign in user using Google account.
     * @param [context] Activity context.
     * @return true if user is a new user, false if otherwise.
     */
    fun signInWithGoogleCredential(context: Context): Flow<LoginResultType>

    /**
     * Sign up user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return true if user is created successfully, false if otherwise.
     */
    fun signUpWithEmail(email: String, password: String): Flow<LoginResultType>

    /**
     * Sign in user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return [Result.Success] if user is signed in successfully, [Result.Error] otherwise.
     */
    fun signInWithEmail(email: String, password: String): Flow<Result<Unit>>
}
