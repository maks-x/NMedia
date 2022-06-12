package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsFeedAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.clearInputArea
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postsFeedAdapter = PostsFeedAdapter(viewModel)

        with(binding.recyclerView) {
            adapter = postsFeedAdapter
            viewModel.data.observe(this@MainActivity) { posts ->
                postsFeedAdapter.submitList(posts) {
                    viewModel.scrollOnTop?.runIfNotHandled { this.scrollToPosition(top) }
                }
            }
        }

        binding.savePostButton.setOnClickListener {
            with(binding) {
                val content = inputText.text.toString()
                viewModel.onSaveButtonClick(content)
                clearInputArea()
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding) {
                inputText.setText(currentPost?.content)
                currentPost?.let { editingGroup.visibility = View.VISIBLE }
            }
        }

        binding.cancelEditButton.setOnClickListener {
            viewModel.onCancelEditingClick()
            binding.clearInputArea()
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