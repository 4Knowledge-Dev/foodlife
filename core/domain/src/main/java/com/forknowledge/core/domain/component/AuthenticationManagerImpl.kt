package com.forknowledge.core.domain.component

import android.content.Context
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.forknowledge.core.common.Result
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialsManager,
    private val facebookCallbackManager: CallbackManager,
) : AuthenticationManager {

    override fun signInWithGoogleCredential(context: Context) = flow {
        try {
            val token = credentialManager.googleSignInCredentials(context)
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth
                .signInWithCredential(credential)
                .await()
            emit(Result.Success(Unit))

        } catch (e: FirebaseAuthException) {
            emit(
                Result.Error(Exception("Failed to log user in: ${e.message}", e))
            )
        } catch (e: Exception) {
            emit(
                Result.Error(Exception("Unexpected error occurred: ${e.message}", e))
            )
        }
    }

    override fun signInWithFacebookCredential() = callbackFlow {
        LoginManager.getInstance().registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    trySend(
                        Result.Error(Exception("Canceled by user!"))
                    )
                }

                override fun onError(error: FacebookException) {
                    trySend(
                        Result.Error(
                            Exception("Failed to log user in: ${error.message}", error)
                        )
                    )
                }

                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                trySend(Result.Success(Unit))
                            } else {
                                task.exception?.let { e ->
                                    trySend(
                                        Result.Error(
                                            Exception("Failed to log user in: ${e.message}", e)
                                        )
                                    )
                                } ?: trySend(
                                    Result.Error(
                                        Exception("Failed to log user in")
                                    )
                                )
                            }
                        }
                }
            }
        )

        awaitClose { LoginManager.getInstance().unregisterCallback(facebookCallbackManager) }
    }

    override fun signUpWithEmail(
        email: String,
        password: String,
    ) = flow {
        try {
            auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            emit(Result.Success(Unit))
        } catch (e: FirebaseAuthException) {
            emit(
                Result.Error(Exception("Failed to create user: ${e.message}", e))
            )
        } catch (e: Exception) {
            emit(
                Result.Error(Exception("Unexpected error occurred: ${e.message}", e))
            )
        }
    }

    override fun signInWithEmail(
        email: String,
        password: String,
    ) = flow {
        try {
            auth
                .signInWithEmailAndPassword(email, password)
                .await()
            emit(Result.Success(Unit))
        } catch (e: FirebaseAuthException) {
            emit(
                Result.Error(Exception("Failed to log user in: ${e.message}", e))
            )
        } catch (e: Exception) {
            emit(
                Result.Error(Exception("Unexpected error occurred: ${e.message}", e))
            )
        }
    }
}
