package com.example.hektagramstory.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hektagramstory.R
import com.example.hektagramstory.databinding.ActivityLoginBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.ui.register.RegisterActivity
import com.example.hektagramstory.utils.LoadingDialog
import com.example.hektagramstory.utils.SharedPreferencesManager

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val userPref = SharedPreferencesManager(this)
        playAnimation()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        val viewModel: LoginViewModel by viewModels {
            factory
        }

        val actionBar = supportActionBar
        actionBar?.hide()
        val loadingDialog = LoadingDialog(this@LoginActivity)

        binding?.apply {
            tvRegister.setOnClickListener {
                val move = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(move)
            }

            btnLogin.setOnClickListener {
                if (edtEmail.text!!.isNotEmpty()) {
                    loadingDialog.startLoadingDialog()
                    viewModel.loginUser(
                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                        this@LoginActivity,
                        loadingDialog,
                        userPref
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.empty_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun playAnimation() {
        val welcome = ObjectAnimator.ofFloat(binding?.tvWelcome, View.ALPHA, 1f).setDuration(600)
        val email = ObjectAnimator.ofFloat(binding?.edtEmail, View.ALPHA, 1f).setDuration(600)
        val password = ObjectAnimator.ofFloat(binding?.edtPassword, View.ALPHA, 1f).setDuration(600)
        val button = ObjectAnimator.ofFloat(binding?.btnLogin, View.ALPHA, 1f).setDuration(600)
        val askRegister =
            ObjectAnimator.ofFloat(binding?.askRegister, View.ALPHA, 1f).setDuration(600)
        val register = ObjectAnimator.ofFloat(binding?.tvRegister, View.ALPHA, 1f).setDuration(600)


        AnimatorSet().apply {
            playSequentially(welcome, email, password, button, askRegister, register)
            start()
        }
    }

}