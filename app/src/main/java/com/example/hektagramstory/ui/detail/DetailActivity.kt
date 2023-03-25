package com.example.hektagramstory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.hektagramstory.R
import com.example.hektagramstory.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        val actionBar : ActionBar? = supportActionBar
        actionBar?.title = resources.getString(R.string.detail_story)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        var name: String? = intent.getStringExtra(EXTRA_NAME)
        var desc: String? = intent.getStringExtra(EXTRA_DESC)
        var poster: String? = intent.getStringExtra(EXTRA_POSTER)

        binding?.apply {
            tvName.text = name
            tvDesc.text = desc
            Glide.with(this@DetailActivity).load(poster).centerCrop().into(ivPoster)
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_POSTER = "extra_poster"
    }
}