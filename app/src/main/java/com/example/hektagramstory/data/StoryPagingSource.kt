package com.example.hektagramstory.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hektagramstory.data.remote.response.GetAllStoriesResponse
import com.example.hektagramstory.data.remote.response.ListStoryItem
import com.example.hektagramstory.data.remote.retrofit.ApiService
import com.example.hektagramstory.utils.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class StoryPagingSource(
    private val apiService: ApiService, private val token: String,
    private val location: Int? = null
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getAllStories(token, position, params.loadSize, location = location)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }


}