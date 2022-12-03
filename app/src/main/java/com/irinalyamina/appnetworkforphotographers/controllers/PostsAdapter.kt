package com.irinalyamina.appnetworkforphotographers.controllers

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.R

class PostsAdapter(private val images: List<Bitmap>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    /*fun updateImages(newImages: List<String>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(images, newImages) { it })
        this.images = newImages
        diffResult.dispatchUpdatesTo(this)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val image = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false) as ImageView
        return ViewHolder(image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])
    }

    private fun ImageView.loadImage(image: Bitmap) {
        //Glide.with(this).load(image).centerCrop().into(this)
    }

    override fun getItemCount(): Int = images.size
}