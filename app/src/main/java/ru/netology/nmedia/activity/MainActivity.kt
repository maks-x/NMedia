package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsFeedAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsFeedAdapter(viewModel)

        binding.recyclerView.adapter = adapter

        viewModel.data.observe(this@MainActivity) { posts ->
            val isNewPost = posts.size > adapter.itemCount
            adapter.submitList(posts) {
                if (isNewPost) binding.recyclerView.run {
                    scrollToPosition(top)
                }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.onAddButtonClick()
        }

        viewModel.sharePostContentEvent.observe(this) {
            it.getContentIfNotHandled()
                ?.let { postContent ->
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, postContent)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(
                        intent, getString(R.string.chooser_share_post)
                    )
                    startActivity(shareIntent)
                }
        }

        val postContentActivityLauncher =
            registerForActivityResult(PostContentActivityResultContract()) { postContent ->
                postContent ?: return@registerForActivityResult
                postContent.map { it ?: "" }
                    .toTypedArray()
                    .let {
                        viewModel.savePost(it)
                    }
            }

        viewModel.navigateToPostContentActivityEvent.observe(this) {
            it.getContentIfNotHandled()?.let { postContent ->
                postContentActivityLauncher.launch(postContent)
            }
        }

        viewModel.videoPlayEvent.observe(this) {
            it.getContentIfNotHandled()
                .let { url ->
                    url ?: return@let
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
        }
    }
}