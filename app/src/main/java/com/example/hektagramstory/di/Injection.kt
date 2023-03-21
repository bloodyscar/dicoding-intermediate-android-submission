package com.example.hektagramstory.di

import android.content.Context
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.retrofit.ApiConfig
import com.example.hektagramstory.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiServices()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, appExecutors)
    }
}