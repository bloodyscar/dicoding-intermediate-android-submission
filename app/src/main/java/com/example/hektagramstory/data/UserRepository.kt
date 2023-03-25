package com.example.hektagramstory.data

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.hektagramstory.data.remote.retrofit.ApiService
import com.example.hektagramstory.data.remote.response.GetAllStoriesResponse
import com.example.hektagramstory.data.remote.response.ListStoryItem
import com.example.hektagramstory.data.remote.response.LoginResponse
import com.example.hektagramstory.data.remote.response.RegisterResponse
import com.example.hektagramstory.ui.home.HomeActivity
import com.example.hektagramstory.ui.login.LoginActivity
import com.example.hektagramstory.ui.story.AddStoryActivity
import com.example.hektagramstory.utils.LoadingDialog
import com.example.hektagramstory.utils.SharedPreferencesManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val apiService: ApiService,
) {

    fun getAllStories(
        token: String
    ): LiveData<Result<List<ListStoryItem>>> {
        val listStories = MediatorLiveData<Result<List<ListStoryItem>>>()

        listStories.postValue(Result.Loading)
        val client = apiService.getAllStories(token)
        client.enqueue(object : Callback<GetAllStoriesResponse> {
            override fun onResponse(
                call: Call<GetAllStoriesResponse>,
                response: Response<GetAllStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listStories.value = Result.Success(responseBody.listStory)
                    }
                } else {
                    listStories.postValue(Result.Error("Error ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetAllStoriesResponse>, t: Throwable) {
                listStories.postValue(Result.Error(t.message.toString()))
            }
        })

        return listStories

    }

    fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        loadingDialog: LoadingDialog,
        activity: AddStoryActivity
    ) {
        val client = apiService.postStory(token, file, description)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    loadingDialog.dismiss()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val message = responseBody.message
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                } else {
                    loadingDialog.dismiss()
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d(AddStoryActivity.NAME_ACTIVITY, t.message.toString())
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        })
    }

    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        loadingDialog: LoadingDialog,
        userPref: SharedPreferencesManager
    ) {
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loadingDialog.dismiss()
                        userPref.setUser(responseBody.loginResult!!)
                        val intent = Intent(activity, HomeActivity::class.java)
                        activity.finish()
                        activity.startActivity(intent)
                    }
                } else {
                    loadingDialog.dismiss()
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Log.d(LoginActivity.NAME_ACTIVITY, t.message.toString())
            }

        })
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }

}


