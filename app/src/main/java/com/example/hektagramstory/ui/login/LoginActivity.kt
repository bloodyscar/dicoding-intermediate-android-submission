package com.example.hektagramstory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.hektagramstory.databinding.ActivityLoginBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.ui.home.HomeActivity
import com.example.hektagramstory.ui.register.RegisterActivity
import com.example.hektagramstory.utils.LoadingDialog
import com.example.hektagramstory.utils.SharedPreferencesManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPref = SharedPreferencesManager(this)






        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        val viewModel: LoginViewModel by viewModels {
            factory
        }

        val actionBar = supportActionBar
        actionBar?.hide()
        val loadingDialog: LoadingDialog = LoadingDialog(this@LoginActivity)

        binding.apply {
            tvRegister.setOnClickListener{
                val move = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(move)
            }



            btnLogin.setOnClickListener{
                loadingDialog.startLoadingDialog()
                viewModel.loginUser(edtEmail.text.toString(), edtPassword.text.toString(), this@LoginActivity, loadingDialog, userPref)

            }
        }
    }

    companion object{
        const val NAME_ACTIVITY = "Login4ctivity"
    }
}