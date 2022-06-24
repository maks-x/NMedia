package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.utils.SingleEvent

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data

    private var currentPost: Post? = null

    val navigateToPostContentActivityEvent = MutableLiveData<SingleEvent<String>>()

    val sharePostContentEvent = MutableLiveData<SingleEvent<String>>()

    val videoPlayEvent = MutableLiveData<SingleEvent<String>>()
    fun savePost(content: Array<String>) {
        if (content[0].isBlank()) return
        if (content[1].isBlank()) {
            savePost(content[0])
            return
        }

        val newOrEditedPost =
            currentPost?.copy(
                content = content[0],
                videoLink = content[1]
            ) ?: Post(
                content = content[0],
                videoLink = content[1]
            )
        repository.save(newOrEditedPost)
    }

    fun savePost(content: String) {
        if (content.isBlank()) return

        val newOrEditedPost =
            currentPost?.copy(
                content = content
            ) ?: Post(
                content = content
            )
        repository.save(newOrEditedPost)
    }

    fun onAddButtonClick() {
        currentPost = null
        navigateToPostContentActivityEvent.value = SingleEvent(Post.EMPTY_POST_CONTENT)
    }

    // region PostInteractionListener

    override fun onLikeClick(postID: Long) = repository.like(postID)
    override fun onShareClick(post: Post) {
        sharePostContentEvent.value = SingleEvent(post.content)
        repository.share(post)
    }
    override fun onRemoveClick(postID: Long) = repository.remove(postID)
    override fun onEditClick(post: Post) {
        currentPost = post
        navigateToPostContentActivityEvent.value = SingleEvent(post.content)
    }
    override fun onVideoLinkClick(link: String) {
        videoPlayEvent.value = SingleEvent((link))
    }

    // endregion PostInteractionListener
}
