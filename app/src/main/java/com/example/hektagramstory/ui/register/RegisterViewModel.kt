package com.example.hektagramstory.ui.register

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.remote.response.RegisterResponse
import com.example.hektagramstory.data.remote.retrofit.ApiConfig
import com.example.hektagramstory.utils.LoadingDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
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
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loadingDialog.dismiss()
                        Toast.makeText(activity, responseBody.message, Toast.LENGTH_SHORT)
                            .show()
                        activity.finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }
}