package com.utsman.hiyahiyahiya.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.inlineactivityresult.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.utils.logi

class AuthRepository(context: Context) {
    var googleSignInClient: GoogleSignInClient
    var firebaseAuth: FirebaseAuth

    init {
        logi("init gso ----------->")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        firebaseAuth = Firebase.auth
    }

    fun requestUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun login(activity: AppCompatActivity, userResult: (FirebaseUser?, String?) -> Unit) {
        val intent = googleSignInClient.signInIntent
        activity.startActivityForResult(intent) { success, data ->
            if (success) {
                logi("success")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                userResult.invoke(user, null)
                            } else {
                                userResult.invoke(null, "Login failed, user null")
                            }
                        }
                } catch (e: ApiException) {
                    userResult.invoke(null, e.localizedMessage)
                }
            } else {
                logi("failed")
                userResult.invoke(null, "failed")
            }
        }
    }
}