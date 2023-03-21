package com.example.hektagramstory

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hektagramstory.ui.home.HomeActivity
import com.example.hektagramstory.ui.login.LoginActivity
import com.example.hektagramstory.utils.SharedPreferencesManager

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val actionBar = supportActionBar
        actionBar?.hide()

        val userPref = SharedPreferencesManager(this)

        Log.d(NAME_ACTIVITY, (userPref.getUser() != null && userPref.getUser() != "").toString())
        Log.d(NAME_ACTIVITY, userPref.getUser().toString())

        if(userPref.getUser() != null && userPref.getUser() != ""){
            Handler().postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        }
    }

    companion object {
        const val NAME_ACTIVITY = "SplashScr33n"
    }
}