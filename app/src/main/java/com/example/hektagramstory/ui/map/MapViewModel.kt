package com.example.hektagramstory.ui.map

import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository

class MapViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllStoriesLocation(
        token: String,
        location: Int
    ) =
        userRepository.getAllStoriesLocation(token, location)
}