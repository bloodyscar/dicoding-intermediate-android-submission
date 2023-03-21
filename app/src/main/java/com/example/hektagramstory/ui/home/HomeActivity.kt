package com.example.hektagramstory.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hektagramstory.databinding.ActivityHomeBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.SharedPreferencesManager
import com.example.hektagramstory.data.Result
import com.example.hektagramstory.ui.story.AddStoryActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvStories: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        val actionBar = supportActionBar
        actionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: HomeViewModel by viewModels {
            factory
        }

        sharedPreferencesManager = SharedPreferencesManager(this)
        val user = sharedPreferencesManager.getUser()

        rvStories = binding.rvHome

        rvStories.setHasFixedSize(true)
        rvStories.layoutManager = LinearLayoutManager(this)



        viewModel.getAllStories("Bearer $user").observe(this){result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.shimmer.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.shimmer.visibility = View.GONE
                        Log.d(NAME_ACTIVITY, result.data.size.toString())

                        rvStories.adapter = ListStoriesAdapter(result.data)
                    }
                    is Result.Error -> {
                        Log.d(NAME_ACTIVITY, result.error)
                        binding.shimmer.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }





    }

    companion object {
        const val NAME_ACTIVITY = "Hom4ctivity"
    }
}