package com.example.hektagramstory.di

import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiServices()
        return UserRepository.getInstance(apiService)
    }
}