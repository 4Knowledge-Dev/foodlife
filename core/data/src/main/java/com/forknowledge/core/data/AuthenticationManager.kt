package com.forknowledge.core.data

import android.content.Context
import com.forknowledge.core.data.datatype.LoginResultType
import kotlinx.coroutines.flow.Flow

interface AuthenticationManager {

    /**
     * Sign in user using Google account.
     * @param [context] Activity context.
     * @return [com.forknowledge.core.data.datatype.LoginResultType] result of sign in process.
     */
    fun signInWithGoogleCredential(context: Context): Flow<LoginResultType>

    /**
     * Sign up user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return [LoginResultType] result of sign up process.
     */
    fun signUpWithEmail(email: String, password: String): Flow<LoginResultType>

    /**
     * Sign in user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return [LoginResultType] result of sign in process.
     */
    fun signInWithEmail(email: String, password: String): Flow<LoginResultType>
}
