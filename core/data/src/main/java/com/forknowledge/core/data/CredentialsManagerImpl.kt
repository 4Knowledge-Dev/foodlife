package com.forknowledge.core.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

private const val GOOGLE_TOKEN_TAG = "GoogleIdToken"

class CredentialsManagerImpl @Inject constructor(
    @ApplicationContext context: Context
) : CredentialsManager {

    private val credentialsManager = CredentialManager.create(context)
    private val nonce: String
    private var googleIdOption: GetGoogleIdOption

    init {
        // Generate a nonce for the request.
        val rawNonce = UUID.randomUUID().toString()
        val byteNonce = rawNonce.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(byteNonce)
        nonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setAutoSelectEnabled(true)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setNonce(nonce)
            .build()
    }

    /**
     * Get a credential selected by user.
     * @return tokenId used to authorize with server-side if has any account selected by use,
     * null if otherwise.
     */
    override suspend fun signInWithAvailableCredentials(context: Context): String? {
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialsManager.getCredential(
                context = context,
                request = credentialRequest,
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Log.e(GOOGLE_TOKEN_TAG, "Unexpected credential!", e)
            handleFailure(context, e)
        }
    }

    /**
     * Get a credential selected by user.
     * @return googleTokenId used to authorize with server-side if has any account selected by use,
     * null if otherwise.
     */
    override suspend fun googleSignInCredentials(context: Context): String? {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_CLIENT_ID)
            .setNonce(nonce)
            .build()
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return try {
            val result = credentialsManager.getCredential(
                context = context,
                request = credentialRequest,
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Log.e(GOOGLE_TOKEN_TAG, "Unexpected credential!", e)
            null
        }
    }

    /**
     * Get googleTokenId used to authorize with server-side.
     * @param [result] credential saved in system.
     * @return googleTokenId.
     */
    private fun handleSignIn(result: GetCredentialResponse): String? {
        return when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        googleIdTokenCredential.idToken
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(GOOGLE_TOKEN_TAG, "Received an invalid google id token response", e)
                        null
                    }
                } else {
                    Log.e("GoogleIdToken", "Unexpected type of credential")
                    return null
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(GOOGLE_TOKEN_TAG, "Unexpected type of credential")
                null
            }
        }
    }

    /**
     * Handle credential failure. If the exception is [androidx.credentials.exceptions.GetCredentialCancellationException] then return null.
     * Otherwise, try to get another credential.
     * @param [context] context of activity
     * @param [exception] credential exception.
     * @return googleTokenId used to authorize with server-side if has any account selected by use.
     */
    private suspend fun handleFailure(
        context: Context,
        exception: GetCredentialException
    ): String? {
        return when (exception) {
            is GetCredentialCancellationException, is NoCredentialException -> null
            else -> {
                googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setNonce(nonce)
                    .build()
                signInWithAvailableCredentials(context)
            }
        }
    }
}
