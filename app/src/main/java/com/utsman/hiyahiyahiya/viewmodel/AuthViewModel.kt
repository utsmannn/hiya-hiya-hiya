package com.utsman.hiyahiyahiya.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import com.utsman.hiyahiyahiya.viewmodel.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository =
        AuthRepository(application.applicationContext)

    fun initGoogleAuth() = repository.initAuth()
    fun login(activity: AppCompatActivity, userResult: (FirebaseUser?, String?) -> Unit) = repository.login(activity, userResult)
}