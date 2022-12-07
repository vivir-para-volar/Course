package com.irinalyamina.appnetworkforphotographers.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.databinding.PostItemBinding
import com.irinalyamina.appnetworkforphotographers.models.Post

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    private var listPosts: ArrayList<Post> = arrayListOf()

    class PostHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = PostItemBinding.bind(item)

        fun bind(post: Post){
            binding.photographerProfilePhoto.setImageBitmap(post.photographerProfilePhoto)
            binding.photographerUsername.text = post.photographerUsername
            binding.uploadDate.text = Parse.dateToString(post.uploadDate)
            binding.postPhoto.setImageBitmap(post.photo)
            binding.textCaption.text = post.caption
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(listPosts[position])
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    fun setListPosts(listPosts: ArrayList<Post>){
        this.listPosts = listPosts
        notifyDataSetChanged()
    }
}