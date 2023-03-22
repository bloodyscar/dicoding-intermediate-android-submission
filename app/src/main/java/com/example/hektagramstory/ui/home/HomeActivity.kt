package com.example.hektagramstory.ui.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hektagramstory.R
import com.example.hektagramstory.databinding.ActivityHomeBinding
import com.example.hektagramstory.ui.ViewModelFactory
import com.example.hektagramstory.utils.SharedPreferencesManager
import com.example.hektagramstory.data.Result
import com.example.hektagramstory.ui.login.LoginActivity
import com.example.hektagramstory.ui.story.AddStoryActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: HomeViewModel by viewModels{
        factory
    }
    private lateinit var listStoriesAdapter : ListStoriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        binding?.fab?.setOnClickListener{
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding?.logout?.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.logout))
            builder.setMessage(resources.getString(R.string.logout_desc))
            builder.setPositiveButton("OK") { dialog, _ ->
                val sharedPref = SharedPreferencesManager(this)
                sharedPref.removeToken()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
            builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                // do something when Cancel button is clicked
            }
            val dialog = builder.create()
            dialog.show()
        }

        val actionBar = supportActionBar
        actionBar?.hide()

        listStoriesAdapter = ListStoriesAdapter()


        sharedPreferencesManager = SharedPreferencesManager(this)
        val user = sharedPreferencesManager.getUser()





        viewModel.getAllStories("Bearer $user").observe(this){result ->
            Log.d(NAME_ACTIVITY, "ONCREATE")
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
                            setHasFixedSize(true)
                            adapter = listStoriesAdapter
                        }


                    }
                    is Result.Error -> {
                        binding?.shimmer?.visibility = View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRestart() {
        super.onRestart()
        val token = sharedPreferencesManager.getUser()
        if(token != null) {
            viewModel.getAllStories("Bearer $token").observe(this){result ->
                Log.d(NAME_ACTIVITY, "ONCREATE")
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
                                setHasFixedSize(true)
                                adapter = listStoriesAdapter
                            }


                        }
                        is Result.Error -> {
                            binding?.shimmer?.visibility = View.GONE
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
    }

    companion object {
        const val NAME_ACTIVITY = "Hom4ctivity"
    }
}