package com.forknowledge.core.domain.component

import android.content.Context
import com.forknowledge.core.common.Result
import kotlinx.coroutines.flow.Flow

interface AuthenticationManager {

    /**
     * Sign in user using Google account.
     * @param [context] Activity context.
     * @return [Result.Success] if user is signed in successfully, [Result.Error] otherwise.
     */
    fun signInWithGoogleCredential(context: Context): Flow<Result<Unit>>

    /**
     * Sign in user using Facebook account.
     * @return [Result.Success] if user is signed in successfully, [Result.Error] otherwise.
     */
    fun signInWithFacebookCredential(): Flow<Result<Unit>>

    /**
     * Sign up user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return [Result.Success] if user is created successfully, [Result.Error] otherwise.
     */
    fun signUpWithEmail(email: String, password: String): Flow<Result<Unit>>

    /**
     * Sign in user using Password-based account.
     * @param [email] user's email account.
     * @param [password] account's password.
     * @return [Result.Success] if user is signed in successfully, [Result.Error] otherwise.
     */
    fun signInWithEmail(email: String, password: String): Flow<Result<Unit>>
}
