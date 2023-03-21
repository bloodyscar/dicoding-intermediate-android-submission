package com.example.hektagramstory.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import com.example.hektagramstory.customview.CustomEditText
import com.example.hektagramstory.databinding.ActivityRegisterBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.LoadingDialog

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtPassword: CustomEditText
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
            edtPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (char?.length!! < 8 && char?.isNotEmpty()!!) {
                        edtPassword.error = "Password minimal 8 karakter"
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

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