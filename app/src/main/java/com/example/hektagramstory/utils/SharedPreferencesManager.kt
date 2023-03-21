package com.example.hektagramstory.utils

import android.content.Context
import com.example.hektagramstory.data.remote.response.LoginResult


class SharedPreferencesManager(private val context: Context) {
    private val preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    companion object {
        const val TOKEN_KEY = "token"
    }

    fun setUser(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(TOKEN_KEY, value.token)
        editor.apply()
    }

    fun getUser(): String? {
        return preferences.getString(TOKEN_KEY, "")
    }

}