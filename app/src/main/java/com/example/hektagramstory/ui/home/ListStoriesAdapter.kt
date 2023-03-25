package com.example.hektagramstory.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hektagramstory.R
import com.example.hektagramstory.data.remote.response.ListStoryItem
import com.example.hektagramstory.databinding.ItemHomeBinding

class ListStoriesAdapter(private val onListStoryClick: (ListStoryItem) -> Unit) :
    ListAdapter<ListStoryItem, ListStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onListStoryClick(data)
        }
    }

    class MyViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvName.text = story.name
            binding.tvDesc.text = story.description
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.place)
            Glide.with(itemView.context).load(story.photoUrl).apply(options)
                .into(binding.ivPoster)

        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldUser: ListStoryItem,
                    newUser: ListStoryItem
                ): Boolean {
                    return oldUser.name == newUser.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: ListStoryItem,
                    newUser: ListStoryItem
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }


}