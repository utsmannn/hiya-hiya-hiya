package com.utsman.hiyahiyahiya.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository =
        AuthRepository(application.applicationContext)

    fun requestUser() = repository.requestUser()
    fun login(activity: AppCompatActivity, userResult: (FirebaseUser?, String?) -> Unit) = repository.login(activity, userResult)
}