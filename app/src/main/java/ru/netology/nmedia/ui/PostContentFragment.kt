package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostContentBinding
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
    ) = FragmentPostContentBinding.inflate(inflater, container, false)
        .also { binding ->
            val post = navArgs<PostContentFragmentArgs>().value.post

            with(binding) {
                makeVisibleAndFocus(TEXT)
                avatar.setImageResource(post.avatarID)
                author.text = post.author
                published.text = post.published
                with(editText) {
                    setText(post.text)
                    showKeyboard()
                }
                post.videoLink?.let { editLink.setText(it) }

                close.setOnClickListener {
                    findNavController().popBackStack()
                }

                toLink.setOnClickListener {
                    makeVisibleAndFocus(LINK)
                }

                toText.setOnClickListener {
                    makeVisibleAndFocus(TEXT)
                }

                ok.setOnClickListener {
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
        }.root

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
