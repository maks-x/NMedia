package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.ui.MainActivity.Companion.INNER_INTENT_IDENTIFIER
import ru.netology.nmedia.utils.fillWithPost
import ru.netology.nmedia.utils.sharePostWithIntent
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.postFragmentEditEvent.observe(this) {
            it.getContentIfNotHandled()?.let { post ->
                val direction =
                    PostFragmentDirections.postFragmentToPostContentFragment(post)
                findNavController().navigate(direction)
            }
        }

        viewModel.postFragmentShareEvent.observe(this) {
            it.getContentIfNotHandled()
                ?.let { postContent ->
                    sharePostWithIntent(postContent, INNER_INTENT_IDENTIFIER)
                }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            with(binding.post) {

                val currentPostID = navArgs<PostFragmentArgs>().value.currentPostID

                lateinit var post: Post

                viewModel.data.observe(viewLifecycleOwner) { posts ->
                    post = posts.first { it.id == currentPostID }
                    fillWithPost(post)
                }

                val postPopupMenu by lazy {
                    PopupMenu(root.context, postsOptions).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.popupRemove -> {
                                    viewModel.onPostFragmentRemove(post.id)
                                    findNavController().popBackStack()
                                    true
                                }
                                R.id.popupEdit -> {
                                    viewModel.onPostFragmentEdit(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }
                }

                likes.setOnClickListener { viewModel.onLikeClick(post.id) }
                share.setOnClickListener { viewModel.onPostFragmentShare(post) }
                postsOptions.setOnClickListener { postPopupMenu.show() }
                videoPlay.setOnClickListener {
                    post.videoLink?.let { viewModel.onVideoLinkClick(it) }
                }
                videoPreview.setOnClickListener { videoPlay.performClick() }
            }
        }.root
}