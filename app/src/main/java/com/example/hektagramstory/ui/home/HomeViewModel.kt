package com.example.hektagramstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.data.remote.response.ListStoryItem


class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> =
        userRepository.getAllStories(token).cachedIn(viewModelScope)
}