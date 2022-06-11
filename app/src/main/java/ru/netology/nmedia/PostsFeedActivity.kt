package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostsFeedBinding

class PostsFeedActivity : AppCompatActivity(R.layout.posts_feed) {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val postsFeedBinding = PostsFeedBinding.inflate(layoutInflater)
        setContentView(postsFeedBinding.root)

        val postsFeedAdapter = PostsFeedAdapter(viewModel)

        with(postsFeedBinding.recyclerView) {
            adapter = postsFeedAdapter
            viewModel.data.observe(this@PostsFeedActivity) { posts ->
                postsFeedAdapter.submitList(posts) {
                    viewModel.scrollOnTop?.runIfNotHandled { this.scrollToPosition(top) }
                }
            }
        }

        postsFeedBinding.savePostButton.setOnClickListener {
            with(postsFeedBinding) {
                val content = inputText.text.toString()
                viewModel.onSaveButtonClick(content)
                clearInputArea()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(postsFeedBinding) {
                inputText.setText(currentPost?.content)
                currentPost?.let { editingGroup.visibility = View.VISIBLE }
            }
        }

        postsFeedBinding.cancelEditButton.setOnClickListener {
            viewModel.onCancelEditingClick()
            postsFeedBinding.clearInputArea()
        }

        viewModel.sharePostContent.observe(this) {
            it.getContentIfNotHandled()?.let { postContent ->
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
    }
}