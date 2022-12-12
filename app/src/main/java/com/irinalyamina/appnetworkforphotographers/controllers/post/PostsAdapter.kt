package com.irinalyamina.appnetworkforphotographers.controllers.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.databinding.PostItemBinding
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService
import de.hdodenhof.circleimageview.CircleImageView

class PostsAdapter(private val context: Context) : RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    private var listPosts: ArrayList<Post> = arrayListOf()

    class PostHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = PostItemBinding.bind(view)

        fun bind(post: Post) {
            binding.photographerProfilePhoto.setImageBitmap(post.photographerProfilePhoto)
            binding.photographerUsername.text = post.photographerUsername
            binding.uploadDate.text = Parse.dateTimeToString(post.uploadDate)
            binding.postPhoto.setImageBitmap(post.photo)
            binding.textCaption.text = post.caption

            if (post.listLikes.contains(PhotographerService.getCurrentUser().id)) {
                binding.imgLikes.setImageResource(R.drawable.ic_like_click_black)
            }
            binding.textLikes.text = post.listLikes.size.toString()

            binding.textComments.text = post.countComments.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(listPosts[position])

        val post = listPosts[position]
        val postService = PostService(context)

        holder.view.findViewById<TextView>(R.id.photographer_username).setOnClickListener {
            if (post.photographerId == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", post.photographerId)
                intent.putExtra("fromActivity", FromActivity.home)
                context.startActivity(intent)
            }
        }

        holder.view.findViewById<CircleImageView>(R.id.photographer_profile_photo).setOnClickListener {
            if (post.photographerId == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", post.photographerId)
                intent.putExtra("fromActivity", FromActivity.home)
                context.startActivity(intent)
            }
        }

        holder.view.findViewById<ImageView>(R.id.img_likes).setOnClickListener {
            if (post.listLikes.contains(PhotographerService.getCurrentUser().id)) {
                val result =
                    postService.deleteLike(post.id, PhotographerService.getCurrentUser().id)
                if (result) {
                    post.listLikes.remove(PhotographerService.getCurrentUser().id)

                    val imgLikes = holder.view.findViewById<ImageView>(R.id.img_likes)
                    imgLikes.setImageResource(R.drawable.ic_like_black)

                    val textLikes = holder.view.findViewById<TextView>(R.id.text_likes)
                    textLikes.text = post.listLikes.size.toString()
                }
            } else {
                val result = postService.addLike(post.id, PhotographerService.getCurrentUser().id)
                if (result) {
                    post.listLikes.add(PhotographerService.getCurrentUser().id)

                    val imgLikes = holder.view.findViewById<ImageView>(R.id.img_likes)
                    imgLikes.setImageResource(R.drawable.ic_like_click_black)

                    val textLikes = holder.view.findViewById<TextView>(R.id.text_likes)
                    textLikes.text = post.listLikes.size.toString()
                }
            }
        }

        holder.view.findViewById<ImageView>(R.id.img_comments).setOnClickListener {
            val intent = Intent(context, PostCommentsActivity::class.java)
            intent.putExtra("postId", post.id)
            context.startActivity(intent)
        }

        holder.view.findViewById<TextView>(R.id.text_comments).setOnClickListener {
            val intent = Intent(context, PostCommentsActivity::class.java)
            intent.putExtra("postId", post.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    fun setListPosts(listPosts: ArrayList<Post>) {
        this.listPosts.addAll(listPosts)
        notifyDataSetChanged()
    }
}