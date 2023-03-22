package com.example.hektagramstory.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hektagramstory.customview.PasswordEditText
import com.example.hektagramstory.databinding.ActivityRegisterBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.LoadingDialog

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtPassword: PasswordEditText
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Register"

        val loadingDialog: LoadingDialog = LoadingDialog(this@RegisterActivity)


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@RegisterActivity)
        val viewModel: RegisterViewModel by viewModels {
            factory
        }

        binding.apply {

            btnRegister.setOnClickListener {
                loadingDialog.startLoadingDialog()
                viewModel.postRegister(
                    edtName.text.toString(),
                    edtEmail.text.toString(),
                    edtPassword.text.toString(),
                    this@RegisterActivity,
                    loadingDialog
                )
            }
        }
    }


    companion object {
        const val NAME_ACTIVITY = "RegisterActivity - "
    }
}