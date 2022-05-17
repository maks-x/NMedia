package ru.netology.nmedia

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
        postsFeedBinding.recyclerView.adapter = postsFeedAdapter
        viewModel.data.observe(this) { posts ->
            postsFeedAdapter.submitList(posts)
        }

        postsFeedBinding.savePostButton.setOnClickListener {
            with(postsFeedBinding) {
                val content = inputText.text.toString()
                viewModel.onSaveButtonClick(content)

                inputText.clearFocus()
                inputText.hideKeyboard()
                editingGroup.visibility = View.GONE

                if (content.isNotBlank())
                    with(postsFeedBinding.recyclerView) {
                        /*
                        Может вы подскажете, как бы это оптимальнее написать?
                        Как-то чопорно получилось...
                        Скролл с задержкой - без анимации, а без задержки не всегда срабатывает
                        Smooth скролл слишком долго мотает)
                        */
                        postDelayed({ scrollToPosition(top) }, 200)
                    }
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(postsFeedBinding) {
                inputText.setText(currentPost?.content)
                currentPost?.let { editingGroup.visibility = View.VISIBLE }
            }
        }

        postsFeedBinding.cancelEditMode.setOnClickListener {
            viewModel.onCancelEditingClick()
            with (postsFeedBinding){
                inputText.clearFocus()
                inputText.hideKeyboard()
                editingGroup.visibility = View.GONE
            }

        }
    }
}