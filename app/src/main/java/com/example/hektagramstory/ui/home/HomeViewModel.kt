package com.example.hektagramstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.Result
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.response.ListStoryItem


class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {



    fun getAllStories(token: String) = userRepository.getAllStories(token)

}