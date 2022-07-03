package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.utils.fillWithPost
import ru.netology.nmedia.viewModel.PostViewModel

class PostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            with(binding.post) {
                val post = navArgs<PostFragmentArgs>().value.currentPost
                    .also {
                        fillWithPost(it)
                    }

                viewModel.data.observe(viewLifecycleOwner) { posts ->
                    fillWithPost(
                        posts.first { it.id == post.id }
                    )
                }

            }

        }.root

}