package com.example.hektagramstory.data.remote.retrofit

import com.example.hektagramstory.data.remote.response.GetAllStoriesResponse
import com.example.hektagramstory.data.remote.response.LoginResponse
import com.example.hektagramstory.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //    endpoint Register
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    //    endpoint login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // endpoint get all stories
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): GetAllStoriesResponse

    @GET("stories")
    fun getAllStoriesLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = null
    ): Call<GetAllStoriesResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<RegisterResponse>

}