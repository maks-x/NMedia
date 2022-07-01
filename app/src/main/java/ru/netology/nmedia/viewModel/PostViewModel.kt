package ru.netology.nmedia.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.repository.FilePostRepository
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.utils.NEW_OR_EDITED_POST_KEY
import ru.netology.nmedia.utils.SingleEvent

class PostViewModel(
//    private val savedState: SavedStateHandle,
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)
    val data by repository::data

//    private var currentPost: Post? = null

    val navigateToPostContentActivityEvent = MutableLiveData<SingleEvent<Post>>()

    val sharePostContentEvent = MutableLiveData<SingleEvent<String>>()

    val videoPlayEvent = MutableLiveData<SingleEvent<String>>()

    fun savePost(bundle: Bundle) {
        if (bundle.isEmpty) return
        val newOrEditedPost = bundle.getParcelable<Post>(NEW_OR_EDITED_POST_KEY) ?: return
        if (newOrEditedPost.text.isBlank()) return
//        val text = content.getString(POST_CONTENT_TEXT) ?: return
//        val videoLink = content.getString(POST_CONTENT_VIDEO_LINK)

//        val newOrEditedPost =
//            currentPost?.copy(
//                text = text,
//                videoLink = videoLink
//            ) ?: Post(
//                text = text,
//                videoLink = videoLink
//            )
        repository.save(newOrEditedPost)
//
//        currentPost = null
    }

    fun onAddButtonClick() {
//        savedState["post"] = SingleEvent(Post())
//        savedState.getLiveData<SingleEvent<Post>>("post")
//        currentPost = null
        navigateToPostContentActivityEvent.value = SingleEvent(Post())
    }

    // region PostInteractionListener

    override fun onLikeClick(postID: Long) = repository.like(postID)
    override fun onShareClick(post: Post) {
        sharePostContentEvent.value = SingleEvent(post.toString())
        repository.share(post)
    }

    override fun onRemoveClick(postID: Long) = repository.remove(postID)
    override fun onEditClick(post: Post) {
//        currentPost = post
        navigateToPostContentActivityEvent.value = SingleEvent(post)
    }

    override fun onVideoLinkClick(link: String) {
        videoPlayEvent.value = SingleEvent(link)
    }

    // endregion PostInteractionListener
}
