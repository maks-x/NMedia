package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.databinding.FragmentPostContentBinding
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.*
import ru.netology.nmedia.utils.hideKeyboard

class PostContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostContentBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            val postBundle = requireArguments()
            val post = checkNotNull(
                postBundle.getParcelable<Post>(EMPTY_OR_EXISTING_POST_KEY)
            ) { "there is no post in fragment's arguments" }

            with(binding) {

                if (!post.isEmpty()) {
                    edit.setText(post.text)
                    post.videoLink?.let { editLink.setText(it) }
                }

//            edit.requestFocus()
//            showKeyboard()

                ok.setOnClickListener {
                    it.hideKeyboard()
                    if (!edit.text.isNullOrBlank()) {
                        val text = edit.text.toString()
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
                    parentFragmentManager.popBackStack()
                }
            }
        }.root
}
