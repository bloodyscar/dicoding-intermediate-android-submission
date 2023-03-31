package com.example.hektagramstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hektagramstory.data.remote.response.GetAllStoriesResponse
import com.example.hektagramstory.data.remote.response.ListStoryItem
import com.example.hektagramstory.data.remote.retrofit.ApiService
import com.example.hektagramstory.utils.SharedPreferencesManager
import retrofit2.Callback

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        TODO("Not yet implemented")
    }


}