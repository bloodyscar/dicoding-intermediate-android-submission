package com.example.hektagramstory.ui.login

import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.utils.LoadingDialog
import com.example.hektagramstory.utils.SharedPreferencesManager

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        loadingDialog: LoadingDialog,
        userPref: SharedPreferencesManager
    ) =
        userRepository.loginUser(email, password, activity, loadingDialog, userPref)
}