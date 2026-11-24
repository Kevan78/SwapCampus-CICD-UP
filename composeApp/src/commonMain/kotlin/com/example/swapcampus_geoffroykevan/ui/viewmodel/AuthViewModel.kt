package com.example.swapcampus_geoffroykevan.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.swapcampus_geoffroykevan.data.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loginFailed by mutableStateOf(false)

    fun login(): Boolean {
        val account = authRepository.authenticate(email, password)
        loginFailed = account == null
        return account != null
    }
}
