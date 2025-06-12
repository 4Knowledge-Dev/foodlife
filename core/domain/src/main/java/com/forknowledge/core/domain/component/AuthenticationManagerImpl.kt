package com.forknowledge.core.domain.component

import android.content.Context
import android.util.Log
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.FirebaseException.FIREBASE_EXCEPTION
import com.forknowledge.core.data.FirebaseException.FIREBASE_GET_DATA_EXCEPTION
import com.forknowledge.core.data.FirestoreReference.USER_COLLECTION
import com.forknowledge.core.data.FirestoreReference.USER_DOCUMENT_IS_NEW_USER_FIELD
import com.forknowledge.core.domain.LoginResultType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialsManager,
    private val firestore: FirebaseFirestore
) : AuthenticationManager {

    override fun signInWithGoogleCredential(context: Context) = flow {
        try {
            val token = credentialManager.googleSignInCredentials(context)
            val credential = GoogleAuthProvider.getCredential(token, null)
            val userInfo = auth
                .signInWithCredential(credential)
                .await()

            if (userInfo.user != null && userInfo.additionalUserInfo != null) {
                emit(
                    if (userInfo.additionalUserInfo!!.isNewUser) {
                        createUserData(userInfo.user!!.uid)
                    } else {
                        LoginResultType.SUCCESS_OLD_USER
                    }
                )
            } else {
                Log.e(FIREBASE_GET_DATA_EXCEPTION, "No user information founded!")
                auth.signOut()
                emit(LoginResultType.FAIL)
            }
        } catch (e: FirebaseAuthException) {
            Log.e(FIREBASE_EXCEPTION, "Failed to log user in: ${e.message}", e)
            emit(LoginResultType.FAIL)
        } catch (e: Exception) {
            Log.e(FIREBASE_EXCEPTION, "Unexpected error occurred: ${e.message}", e)
            emit(LoginResultType.FAIL)
        }
    }

    override fun signUpWithEmail(
        email: String,
        password: String,
    ) = flow {
        try {
            val userInfo = auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            if (userInfo.user != null && userInfo.additionalUserInfo != null) {
                emit(createUserData(userInfo.user!!.uid))
            } else {
                Log.e(FIREBASE_GET_DATA_EXCEPTION, "No user information founded!")
                auth.signOut()
                emit(LoginResultType.FAIL)
            }
        } catch (e: FirebaseAuthException) {
            Log.e(FIREBASE_EXCEPTION, "Failed to create user: ${e.message}", e)
            emit(LoginResultType.FAIL)
        } catch (e: Exception) {
            Log.e(FIREBASE_EXCEPTION, "Unexpected error occurred: ${e.message}", e)
            emit(LoginResultType.FAIL)
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

    private suspend fun createUserData(userId: String): LoginResultType {
        return try {
            firestore.collection(USER_COLLECTION).document(userId)
                .set(hashMapOf(USER_DOCUMENT_IS_NEW_USER_FIELD to true))
                .await()
            LoginResultType.SUCCESS_NEW_USER
        } catch (e: Exception) {
            Log.e(FIREBASE_EXCEPTION, "Failed to create user data", e)
            auth.currentUser!!.delete()
            LoginResultType.FAIL
        }
    }
}
