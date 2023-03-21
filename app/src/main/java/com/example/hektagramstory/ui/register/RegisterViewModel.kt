package com.example.hektagramstory.ui.register

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.response.RegisterResponse
import com.example.hektagramstory.data.remote.retrofit.ApiConfig
import com.example.hektagramstory.data.remote.retrofit.ApiService
import com.example.hektagramstory.utils.LoadingDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun postRegister(
        name: String,
        email: String,
        password: String,
        activity: RegisterActivity,
        loadingDialog: LoadingDialog
    ) {
        val client = ApiConfig.getApiServices().registerUser(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    var responseBody = response.body()
                    if (responseBody != null) {
                        loadingDialog.dismiss()
                        Log.d(RegisterActivity.NAME_ACTIVITY, responseBody.toString())
                        Toast.makeText(activity, "Berhasil membuat akun!", Toast.LENGTH_SHORT)
                            .show()
                        activity.finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d(RegisterActivity.NAME_ACTIVITY, t.message.toString())
            }

        })
    }
}