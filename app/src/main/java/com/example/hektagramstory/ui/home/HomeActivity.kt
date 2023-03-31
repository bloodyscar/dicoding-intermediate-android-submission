package com.example.hektagramstory.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hektagramstory.R
import com.example.hektagramstory.data.Result
import com.example.hektagramstory.databinding.ActivityHomeBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.ui.detail.DetailActivity
import com.example.hektagramstory.ui.login.LoginActivity
import com.example.hektagramstory.ui.map.MapsActivity
import com.example.hektagramstory.ui.story.AddStoryActivity
import com.example.hektagramstory.utils.SharedPreferencesManager

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: HomeViewModel by viewModels {
        factory
    }
    private lateinit var listStoriesAdapter: ListStoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.fab?.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding?.icSetting?.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding?.icGooglemap?.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding?.logout?.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setMessage(resources.getString(R.string.logout_desc))
            builder.setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                val sharedPref = SharedPreferencesManager(this)
                sharedPref.removeToken()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
            builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
            }
            val dialog = builder.create()
            dialog.show()
        }

        val actionBar = supportActionBar
        actionBar?.hide()

        listStoriesAdapter = ListStoriesAdapter { data ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_NAME, data.name)
            intent.putExtra(DetailActivity.EXTRA_DESC, data.description)
            intent.putExtra(DetailActivity.EXTRA_POSTER, data.photoUrl)
            startActivity(intent)
        }


        sharedPreferencesManager = SharedPreferencesManager(this)
        val token = sharedPreferencesManager.getUser()

        if (token != null) {
            showListStories(token)
        }
    }

    private fun showListStories(token: String) {
        viewModel.getAllStories("Bearer $token").observe(this) { result ->

            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.shimmer?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.shimmer?.visibility = View.GONE
                        val data = result.data
                        listStoriesAdapter.submitList(data)
                        binding?.rvHome?.apply {
                            layoutManager = LinearLayoutManager(this@HomeActivity)
                            Handler().postDelayed({
                                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
                            }, 1000)

                            setHasFixedSize(true)
                            adapter = listStoriesAdapter
                        }
                    }
                    is Result.Error -> {
                        binding?.shimmer?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRestart() {
        super.onRestart()
        val token = sharedPreferencesManager.getUser()
        if (token != null) {
            showListStories(token)
        }
    }

    companion object {
        const val NAME_ACTIVITY = "Hom4ctivity"
    }
}