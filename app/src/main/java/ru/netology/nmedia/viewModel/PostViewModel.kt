package ru.netology.nmedia.viewModel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.utils.POST_CONTENT_TEXT
import ru.netology.nmedia.utils.POST_CONTENT_VIDEO_LINK
import ru.netology.nmedia.utils.SingleEvent
import ru.netology.nmedia.utils.withPostContent

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data

    private var currentPost: Post? = null

    val navigateToPostContentActivityEvent = MutableLiveData<SingleEvent<Bundle>>()

    val sharePostContentEvent = MutableLiveData<SingleEvent<String>>()

    val videoPlayEvent = MutableLiveData<SingleEvent<String>>()

    fun savePost(content: Bundle) {
        if (content.isEmpty) return
        val text = content.getString(POST_CONTENT_TEXT) ?: return
        val videoLink = content.getString(POST_CONTENT_VIDEO_LINK)

        val newOrEditedPost =
            currentPost?.copy(
                text = text,
                videoLink = videoLink
            ) ?: Post(
                text = text,
                videoLink = videoLink
            )
        repository.save(newOrEditedPost)

        currentPost = null
    }

    fun onAddButtonClick() {
        currentPost = null
        navigateToPostContentActivityEvent.value = SingleEvent(Bundle())
    }

    // region PostInteractionListener

    override fun onLikeClick(postID: Long) = repository.like(postID)
    override fun onShareClick(post: Post) {
        sharePostContentEvent.value = SingleEvent(post.text)
        repository.share(post)
    }

    override fun onRemoveClick(postID: Long) = repository.remove(postID)
    override fun onEditClick(post: Post) {
        currentPost = post
        navigateToPostContentActivityEvent.value = SingleEvent(
            Bundle().withPostContent(post)
        )
    }

    override fun onVideoLinkClick(link: String) {
        videoPlayEvent.value = SingleEvent((link))
    }

    // endregion PostInteractionListener
}
