package com.irinalyamina.appnetworkforphotographers.controllers.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.CommentItemBinding
import com.irinalyamina.appnetworkforphotographers.models.PostComment
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import de.hdodenhof.circleimageview.CircleImageView

class PostCommentsAdapter(private val context: Context): RecyclerView.Adapter<PostCommentsAdapter.PostCommentHolder>() {
    private var listPostComments: ArrayList<PostComment> = arrayListOf()

    class PostCommentHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = CommentItemBinding.bind(view)

        fun bind(comment: PostComment) {
            binding.photographerProfilePhoto.setImageBitmap(comment.photographerProfilePhoto)
            binding.photographerUsername.text = comment.photographerUsername
            binding.textComment.text = comment.text
            binding.date.text = Parse.dateTimeToString(comment.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return PostCommentHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentHolder, position: Int) {
        holder.bind(listPostComments[position])

        val comment = listPostComments[position]

        holder.view.findViewById<TextView>(R.id.photographer_username).setOnClickListener {
            if (comment.photographerId == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", comment.photographerId)
                intent.putExtra("fromActivity", FromActivity.home)
                context.startActivity(intent)
            }
        }

        holder.view.findViewById<CircleImageView>(R.id.photographer_profile_photo).setOnClickListener {
            if (comment.photographerId == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", comment.photographerId)
                intent.putExtra("fromActivity", FromActivity.home)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return listPostComments.size
    }

    fun setListPostComments(listPostComments: ArrayList<PostComment>) {
        this.listPostComments.addAll(listPostComments)
        notifyDataSetChanged()
    }

    fun addPostComment(comment: PostComment) {
        this.listPostComments.add(comment)
        notifyDataSetChanged()
    }
}