package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.objects.Post.Companion.DEFAULT_POST_ID
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class PostContentFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostContentBinding.inflate(inflater, container, false)

        val post = navArgs<PostContentFragmentArgs>().value.post
        val isEmptyPost = post.text.isBlank()
        val draft = if (isEmptyPost) viewModel.postDraft else post.text

        with(binding) {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                if (isEmptyPost) viewModel.setDraft(editText.text.toString())
                findNavController().navigateUp()
            }
            makeVisibleAndFocus(TEXT)
            avatar.setImageResource(post.avatarID)
            author.text = post.author
            published.text = post.published
            with(editText) {
                setText(draft)
                showKeyboard()
            }
            post.videoLink?.let { editLink.setText(it) }

            close.setOnClickListener {
                it.hideKeyboard()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            toLink.setOnClickListener {
                makeVisibleAndFocus(LINK)
            }

            toText.setOnClickListener {
                makeVisibleAndFocus(TEXT)
            }

            ok.setOnClickListener {
                if (isEmptyPost) viewModel.setDraft(null)
                it.hideKeyboard()
                if (!editText.text.isNullOrBlank()) {
                    val text = editText.text.toString()
                    val link = with(editLink.text) {
                        if (isNullOrBlank()) null else toString()
                    }

                    val newOrEditedPost = post.copy(
                        text = text,
                        videoLink = link
                    )
                    viewModel.savePost(newOrEditedPost)
                }
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    private companion object {

        fun FragmentPostContentBinding.makeVisibleAndFocus(tag: String) {
            when (tag) {
                TEXT -> {
                    editText.visibility = View.VISIBLE
                    toLink.visibility = View.VISIBLE
                    editLink.visibility = View.INVISIBLE
                    toText.visibility = View.INVISIBLE
                    editText.requestFocus()
                }
                LINK -> {
                    editText.visibility = View.INVISIBLE
                    toLink.visibility = View.INVISIBLE
                    editLink.visibility = View.VISIBLE
                    toText.visibility = View.VISIBLE
                    // почему фокус не устанавливается в конец текста???
                    editLink.requestFocus(View.FOCUS_DOWN)
                }
            }
        }

        const val LINK = "link"
        const val TEXT = "text"

    }
}
