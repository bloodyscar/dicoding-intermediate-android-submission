package com.example.hektagramstory.ui.home

import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository


class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllStories(token: String) = userRepository.getAllStories(token)
}