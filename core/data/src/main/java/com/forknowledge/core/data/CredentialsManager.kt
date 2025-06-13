package com.forknowledge.core.data

import android.content.Context

interface CredentialsManager {

    /**
     * Get current available credentials.
     * @return tokenId used to authorize with server-side if has any account selected by use,
     * null if otherwise.
     */
    suspend fun signInWithAvailableCredentials(context: Context): String?

    /**
     * Get a credential selected by user.
     * @return googleTokenId used to authorize with server-side if has any account selected by use,
     * null if otherwise.
     */
    suspend fun googleSignInCredentials(context: Context): String?
}
