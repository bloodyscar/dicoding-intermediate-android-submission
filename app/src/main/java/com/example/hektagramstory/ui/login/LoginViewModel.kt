package com.example.hektagramstory.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.response.LoginResponse
import com.example.hektagramstory.data.remote.retrofit.ApiConfig
import com.example.hektagramstory.utils.LoadingDialog
import com.example.hektagramstory.utils.SharedPreferencesManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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