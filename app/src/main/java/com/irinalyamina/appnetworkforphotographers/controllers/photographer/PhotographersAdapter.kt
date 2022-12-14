package com.irinalyamina.appnetworkforphotographers.controllers.photographer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.controllers.profile.ProfileActivity
import com.irinalyamina.appnetworkforphotographers.controllers.profile.UserProfileActivity
import com.irinalyamina.appnetworkforphotographers.databinding.PhotographerItemBinding
import com.irinalyamina.appnetworkforphotographers.models.Photographer
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import de.hdodenhof.circleimageview.CircleImageView

class PhotographersAdapter(private val context: Context, private val fromActivity: String) :
    RecyclerView.Adapter<PhotographersAdapter.PhotographerHolder>() {
    private var listPhotographers: ArrayList<Photographer> = arrayListOf()

    class PhotographerHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = PhotographerItemBinding.bind(view)

        fun bind(photographer: Photographer) {
            if (photographer.profilePhoto != null) {
                binding.photographerProfilePhoto.setImageBitmap(photographer.profilePhoto)
            }
            binding.photographerUsername.text = photographer.username
            binding.photographerName.text = photographer.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotographerHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.photographer_item, parent, false)
        return PhotographerHolder(view)
    }

    override fun onBindViewHolder(holder: PhotographerHolder, position: Int) {
        holder.bind(listPhotographers[position])

        val photographer = listPhotographers[position]

        holder.view.findViewById<TextView>(R.id.photographer_username).setOnClickListener {
            if (photographer.id == PhotographerService.getCurrentUser().id) {
                val intent = Intent(context, UserProfileActivity::class.java)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("photographerId", photographer.id)
                intent.putExtra("fromActivity", fromActivity)
                context.startActivity(intent)
            }
        }

        holder.view.findViewById<CircleImageView>(R.id.photographer_profile_photo)
            .setOnClickListener {
                if (photographer.id == PhotographerService.getCurrentUser().id) {
                    val intent = Intent(context, UserProfileActivity::class.java)
                    context.startActivity(intent)
                } else {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("photographerId", photographer.id)
                    intent.putExtra("fromActivity", fromActivity)
                    context.startActivity(intent)
                }
            }
    }

    override fun getItemCount(): Int {
        return listPhotographers.size
    }

    fun setListPhotographers(listPhotographers: ArrayList<Photographer>) {
        this.listPhotographers.addAll(listPhotographers)
        notifyDataSetChanged()
    }
}