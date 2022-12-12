package com.irinalyamina.appnetworkforphotographers.controllers.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.irinalyamina.appnetworkforphotographers.R
import com.irinalyamina.appnetworkforphotographers.ShowMessage
import com.irinalyamina.appnetworkforphotographers.databinding.ActivityPostCommentsBinding
import com.irinalyamina.appnetworkforphotographers.models.PostComment
import com.irinalyamina.appnetworkforphotographers.service.PhotographerService
import com.irinalyamina.appnetworkforphotographers.service.PostService

class PostCommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostCommentsBinding

    private lateinit var postCommentsAdapter: PostCommentsAdapter
    private lateinit var listPostComments: ArrayList<PostComment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        postCommentsAdapter = PostCommentsAdapter(this)
        binding.recyclerViewComments.adapter = postCommentsAdapter

        val postId = intent.getIntExtra("postId", -1)
        if(postId != -1){
            initialDate(postId)

            binding.btnAddComment.setOnClickListener{ btnAddCommentOnClickListener(postId) }
        }
    }

    private fun initialDate(postId: Int) {
        val postService = PostService(this)
        listPostComments = postService.getPostComments(postId)
        if (listPostComments.isNotEmpty()) {
            binding.textCountComments.text = listPostComments.size.toString()
            postCommentsAdapter.setListPostComments(listPostComments)
        }
    }

    private fun btnAddCommentOnClickListener(postId: Int) {
        val text = binding.editTextComment.text.toString().trim()

        if (text.isEmpty()) {
            ShowMessage.toast(this, getString(R.string.text_comment_empty))
            return
        }

        (binding.editTextComment as TextView).text = ""

        val photographer = PhotographerService.getCurrentUser()
        val comment = PostComment(text, postId, photographer.id, photographer.username, photographer.profilePhoto)

        val service = PostService(this)
        val answer = service.addPostComment(comment)

        if (answer) {
            listPostComments.add(comment)
            binding.textCountComments.text = listPostComments.size.toString()
            postCommentsAdapter.addPostComment(comment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}