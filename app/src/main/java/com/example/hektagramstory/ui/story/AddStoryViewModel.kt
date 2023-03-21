package com.example.hektagramstory.ui.story

import androidx.lifecycle.ViewModel
import com.example.hektagramstory.data.UserRepository
import com.example.hektagramstory.utils.LoadingDialog
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun postStory(token: String, file: MultipartBody.Part, description: RequestBody, loadingDialog: LoadingDialog, activity: AddStoryActivity) =
        userRepository.postStory(token, file, description, loadingDialog, activity)


}