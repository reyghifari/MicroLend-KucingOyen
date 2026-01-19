package com.kucingoyen.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthClient(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun signInAndGetToken(webClientId: String): String? {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .setNonce(null)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            // Ambil Token JWT Asli dari Google
            val credential = result.credential
            if (credential is GoogleIdTokenCredential) {
                //hit api
                return credential.idToken
            } else if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return googleIdTokenCredential.idToken
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun signOut() {
        credentialManager.clearCredentialState(androidx.credentials.ClearCredentialStateRequest())
    }
}