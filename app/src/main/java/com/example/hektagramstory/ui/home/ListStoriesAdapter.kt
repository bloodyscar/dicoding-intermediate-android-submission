package com.example.hektagramstory.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hektagramstory.R
import com.example.hektagramstory.data.remote.response.ListStoryItem
import com.example.hektagramstory.databinding.ItemHomeBinding


class ListStoriesAdapter(private val listStories : List<ListStoryItem>) : RecyclerView.Adapter<ListStoriesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemHomeBinding.bind(itemView)
        fun bind(story : ListStoryItem){
            binding.apply {
                tvName.text = story.name
                tvDesc.text = story.description
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.place)

                Glide.with(itemView.context).load(story.photoUrl).centerCrop().apply(options).into(ivPoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listStories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }
}