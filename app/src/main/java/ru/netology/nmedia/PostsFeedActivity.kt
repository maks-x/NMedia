package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostsFeedBinding

class PostsFeedActivity : AppCompatActivity(R.layout.posts_feed) {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postsFeedBinding = PostsFeedBinding.inflate(layoutInflater)
        setContentView(postsFeedBinding.root)

        val postsFeedAdapter = PostsFeedAdapter(
            context = this.applicationContext,
            likeByID = viewModel::likeByID,
            shareByID = viewModel::shareByID
        )

        postsFeedBinding.recyclerView.adapter = postsFeedAdapter

        viewModel.data.observe(this) { posts ->
            postsFeedAdapter.submitList(posts)
        }

    }
}