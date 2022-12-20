package com.irinalyamina.appnetworkforphotographers.controllers.post

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.Parse
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.FromActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.PostItemBinding
import com.irinalyamina.appnetworkforphotographers.models.Post
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService
import de.hdodenhof.circleimageview.CircleImageView

class PostsAdapter(private val context: Context, private val fromActivity: String) :
    RecyclerView.Adapter<PostsAdapter.PostHolder>() {
    private var listPosts: ArrayList<Post> = arrayListOf()

    class PostHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = PostItemBinding.bind(view)

        fun bind(post: Post) {
            if (post.photographerProfilePhoto != null) {
                binding.photographerProfilePhoto.setImageBitmap(post.photographerProfilePhoto)
            }

            binding.photographerUsername.text = post.photographerUsername
            binding.uploadDate.text = Parse.dateTimeToString(post.uploadDate)
            binding.postPhoto.setImageBitmap(post.photo)
            binding.textCaption.text = post.caption

            if (post.listLikes.contains(PhotographerService.getCurrentUser().id)) {
                binding.imgLikes.setImageResource(R.drawable.ic_like_click_black)
            } else {
                binding.imgLikes.setImageResource(R.drawable.ic_like_black)
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

        if (fromActivity == FromActivity.userProfile) {
            holder.view.findViewById<ImageButton>(R.id.btn_delete_post).visibility = View.VISIBLE
            holder.view.findViewById<ImageButton>(R.id.btn_delete_post).setOnClickListener {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                postService.deletePost(post.id)
                                listPosts.removeAt(position)
                                notifyDataSetChanged()
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {}
                        }
                    }

                val builder = AlertDialog.Builder(context)
                builder.setMessage(context.getString(R.string.question))
                    .setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(
                        context.getString(R.string.no),
                        dialogClickListener
                    )
                    .show()
            }
        }

        holder.view.findViewById<TextView>(R.id.photographer_username).setOnClickListener {
            if (post.photographerId == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", post.photographerId)
                intent.putExtra("fromActivity", fromActivity)
                context.startActivity(intent)
            }
        }

        holder.view.findViewById<CircleImageView>(R.id.photographer_profile_photo)
            .setOnClickListener {
                if (post.photographerId == PhotographerService.getCurrentUser().id) {
                    val intent = Intent(context, UserProfileActivity::class.java)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("photographerId", post.photographerId)
                    intent.putExtra("fromActivity", fromActivity)
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
            intent.putExtra("fromActivity", fromActivity)
            context.startActivity(intent)
        }

        holder.view.findViewById<TextView>(R.id.text_comments).setOnClickListener {
            val intent = Intent(context, PostCommentsActivity::class.java)
            intent.putExtra("postId", post.id)
            intent.putExtra("fromActivity", fromActivity)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }

    fun setListPosts(listPosts: ArrayList<Post>) {
        this.listPosts = ArrayList(listPosts)
        notifyDataSetChanged()
    }
}