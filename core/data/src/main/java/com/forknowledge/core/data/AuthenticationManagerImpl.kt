package com.forknowledge.core.data

import android.content.Context
import android.util.Log
import com.forknowledge.core.data.datatype.LoginResultType
import com.forknowledge.core.data.reference.FirebaseException
import com.forknowledge.core.data.reference.FirestoreReference
import com.forknowledge.feature.model.userdata.User
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
                        createUserData(
                            userId = userInfo.user!!.uid,
                            email = userInfo.user!!.email!!
                        )
                    } else {
                        getUserState(userInfo.user!!.uid)
                    }
                )
            } else {
                Log.e(FirebaseException.FIREBASE_GET_DATA_EXCEPTION, "No user information founded!")
                auth.signOut()
                emit(LoginResultType.FAIL)
            }
        } catch (e: FirebaseAuthException) {
            Log.e(FirebaseException.FIREBASE_EXCEPTION, "Failed to log user in: ${e.message}", e)
            emit(LoginResultType.FAIL)
        } catch (e: Exception) {
            Log.e(
                FirebaseException.FIREBASE_EXCEPTION,
                "Unexpected error occurred: ${e.message}",
                e
            )
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
                emit(
                    createUserData(
                        userId = userInfo.user!!.uid,
                        email = userInfo.user!!.email!!
                    )
                )
            } else {
                Log.e(FirebaseException.FIREBASE_GET_DATA_EXCEPTION, "No user information founded!")
                auth.signOut()
                emit(LoginResultType.FAIL)
            }
        } catch (e: FirebaseAuthException) {
            Log.e(FirebaseException.FIREBASE_EXCEPTION, "Failed to create user: ${e.message}", e)
            emit(LoginResultType.FAIL)
        } catch (e: Exception) {
            Log.e(
                FirebaseException.FIREBASE_EXCEPTION,
                "Unexpected error occurred: ${e.message}",
                e
            )
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
            emit(getUserState(auth.currentUser!!.uid))
        } catch (e: FirebaseAuthException) {
            Log.e(FirebaseException.FIREBASE_EXCEPTION, "Failed to log user in: ${e.message}", e)
            emit(LoginResultType.FAIL)
        } catch (e: Exception) {
            Log.e(
                FirebaseException.FIREBASE_EXCEPTION,
                "Unexpected error occurred: ${e.message}",
                e
            )
            emit(LoginResultType.FAIL)
        }
    }

    private suspend fun createUserData(
        userId: String,
        email: String
    ): LoginResultType {
        val user = hashMapOf(
            FirestoreReference.USER_DOCUMENT_IS_NEW_USER_FIELD to true,
            FirestoreReference.USER_DOCUMENT_EMAIL_FIELD to email
        )
        return try {
            firestore.collection(FirestoreReference.USER_COLLECTION).document(userId)
                .set(user)
                .await()
            LoginResultType.SUCCESS_NEW_USER
        } catch (e: Exception) {
            Log.e(FirebaseException.FIREBASE_EXCEPTION, "Failed to create user data", e)
            auth.currentUser!!.delete()
            LoginResultType.FAIL
        }
    }

    private suspend fun getUserState(userId: String): LoginResultType {
        return try {
            val user = firestore.collection(FirestoreReference.USER_COLLECTION).document(userId)
                .get()
                .await()
                .toObject(User::class.java)
            user?.let {
                if (user.isNewUser) {
                    LoginResultType.SUCCESS_NEW_USER
                } else {
                    LoginResultType.SUCCESS_OLD_USER
                }
            } ?: LoginResultType.FAIL
        } catch (e: Exception) {
            Log.e(FirebaseException.FIREBASE_GET_DATA_EXCEPTION, "Failed to get user data", e)
            LoginResultType.FAIL
        }
    }
}