package com.example.hektagramstory.di

import android.content.Context
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiServices()
        return UserRepository.getInstance(apiService)
    }
}