package com.example.hektagramstory.ui.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.example.hektagramstory.databinding.ActivityAddStoryBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.ui.login.LoginViewModel
import com.example.hektagramstory.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private var getFile: File? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            Log.d(NAME_ACTIVITY, myFile.toString())

            getFile = myFile
            binding.ivPreview.visibility = View.VISIBLE
            binding.ivPreview.setImageURI(selectedImg)
        }
    }


    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

//            Silakan gunakan kode ini jika mengalami perubahan rotasi
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            binding.ivPreview.visibility = View.VISIBLE
            binding.ivPreview.setImageBitmap(result)


        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = "Add Story"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ivPreview.visibility = View.GONE

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@AddStoryActivity)
        val viewModel: AddStoryViewModel by viewModels {
            factory
        }

        val loadingDialog: LoadingDialog = LoadingDialog(this@AddStoryActivity)

        sharedPreferencesManager = SharedPreferencesManager(this)
        val token = sharedPreferencesManager.getUser()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener{
            startGallery()
        }

        binding.btnUpload.setOnClickListener{
            loadingDialog.startLoadingDialog()
            if (token != null) {
                uploadStory(viewModel, token, loadingDialog)
            }
        }
    }

    private fun uploadStory(viewModel: AddStoryViewModel, token : String, loadingDialog: LoadingDialog) {
        if(getFile != null){
            val file = getFile as File

            val desc = binding.edtDesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.postStory("Bearer $token", imageMultipart, desc, loadingDialog, this@AddStoryActivity)

        } else {
            loadingDialog.dismiss()
            Toast.makeText(this@AddStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.hektagramstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }





    companion object {
        const val CAMERA_X_RESULT = 200
        const val NAME_ACTIVITY = "Detail4ctivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


}