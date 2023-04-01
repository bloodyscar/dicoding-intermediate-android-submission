package com.example.hektagramstory.ui.story

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.hektagramstory.R
import com.example.hektagramstory.databinding.ActivityAddStoryBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var getFile: File? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding?.apply {
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageURI(selectedImg)
            }
        }
    }


    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = exif(currentPhotoPath)

            binding?.apply {
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(result)
            }


        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.add_story)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.ivPreview?.visibility = View.GONE
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@AddStoryActivity)
        val viewModel: AddStoryViewModel by viewModels {
            factory
        }
        val loadingDialog = LoadingDialog(this@AddStoryActivity)
        sharedPreferencesManager = SharedPreferencesManager(this)
        val token = sharedPreferencesManager.getUser()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


        binding?.apply {
            btnCamera.setOnClickListener {
                val options = arrayOf<CharSequence>(
                    resources.getString(R.string.take_photo),
                    resources.getString(R.string.choose_gallery),
                    "Cancel"
                )
                val builder = AlertDialog.Builder(this@AddStoryActivity)
                builder.setTitle(resources.getString(R.string.add_photo))
                builder.setItems(options) { dialog, item ->
                    if (options[item] == resources.getString(R.string.take_photo)) {
                        startTakePhoto()
                    } else if (options[item] == resources.getString(R.string.choose_gallery)) {
                        startGallery()
                    } else {
                        dialog.dismiss()
                    }
                }
                builder.show()
            }

            btnUpload.setOnClickListener {
                loadingDialog.startLoadingDialog()
                if (token != null) {
                    uploadStory(viewModel, token, loadingDialog)
                }
            }
        }
    }

    private fun uploadStory(
        viewModel: AddStoryViewModel,
        token: String,
        loadingDialog: LoadingDialog,
    ) {
        if (getFile != null) {
            lifecycleScope.launch {
                val file = reduceFileImage(getFile as File)
                val desc =
                    binding?.edtDesc?.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                viewModel.postStory(
                    "Bearer $token",
                    imageMultipart,
                    desc,
                    loadingDialog,
                    this@AddStoryActivity
                )
            }
        } else {
            loadingDialog.dismiss()
            Toast.makeText(
                this@AddStoryActivity,
                resources.getString(R.string.no_file),
                Toast.LENGTH_SHORT
            ).show()
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
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


}