package com.example.hektagramstory

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hektagramstory.databinding.ActivitySplashScreenBinding
import com.example.hektagramstory.ui.home.HomeActivity
import com.example.hektagramstory.ui.login.LoginActivity
import com.example.hektagramstory.utils.SharedPreferencesManager

class SplashScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.hide()
        val userPref = SharedPreferencesManager(this)

        if (userPref.getUser() != null && userPref.getUser() != "") {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        }
    }

}