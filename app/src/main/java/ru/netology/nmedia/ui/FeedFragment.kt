package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsFeedAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        viewModel.navigateToPostContentActivityEvent.observe(this) {
            it.getContentIfNotHandled()?.let { emptyOrExistingPost ->
                val direction = FeedFragmentDirections.toPostContentFragment(emptyOrExistingPost)
                findNavController().navigate(direction)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeedBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            val adapter = PostsFeedAdapter(viewModel)

            binding.recyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner) { posts ->
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
        }.root
}