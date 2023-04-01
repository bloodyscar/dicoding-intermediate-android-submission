package com.example.hektagramstory.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hektagramstory.R
import com.example.hektagramstory.databinding.ActivityRegisterBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.LoadingDialog

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = resources.getString(R.string.register)
        val loadingDialog = LoadingDialog(this@RegisterActivity)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@RegisterActivity)
        val viewModel: RegisterViewModel by viewModels {
            factory
        }
        binding?.apply {
            btnRegister.setOnClickListener {
                if (edtName.text.isNotEmpty() && edtEmail.text!!.isNotEmpty() && edtPassword.text!!.isNotEmpty()) {
                    loadingDialog.startLoadingDialog()
                    viewModel.postRegister(
                        edtName.text.toString(),
                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                        this@RegisterActivity,
                        loadingDialog
                    )
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        resources.getString(R.string.empty_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

}