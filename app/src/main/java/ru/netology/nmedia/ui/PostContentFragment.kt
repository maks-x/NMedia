package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.utils.NEW_OR_EDITED_POST_KEY
import ru.netology.nmedia.utils.RESULT_BUNDLE_KEY
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard

class PostContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostContentBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            val post = navArgs<PostContentFragmentArgs>().value.emptyOrExistingPost

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

                        val resultBundle = Bundle(1)
                            .apply {
                                putParcelable(
                                    NEW_OR_EDITED_POST_KEY,
                                    post.copy(
                                        text = text,
                                        videoLink = link
                                    )
                                )
                            }

                        setFragmentResult(RESULT_BUNDLE_KEY, resultBundle)
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
