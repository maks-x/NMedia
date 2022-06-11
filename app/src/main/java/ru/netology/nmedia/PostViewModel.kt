package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel(), PostInteractionListener {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    var scrollOnTop: SingleEvent<Any>? = null
        private set

    val sharePostContent = MutableLiveData<SingleEvent<String>>()

    fun onSaveButtonClick(content: String) {
        if (content.isBlank()) return

        val newOrEditedPost =
            currentPost.value?.copy(
                content = content
            ) ?: Post(
                id = PostRepository.NEW_POST_ID_CHECKER,
                author = "Who cares?",
                published = "Whenever",
                content = content
            ).also {
                scrollOnTop = SingleEvent()
            }
        repository.save(newOrEditedPost)
        currentPost.value = null
    }

    fun onCancelEditingClick() {
        currentPost.value = null
    }

    // region PostInteractionListener

    override fun onLikeClick(postID: Long) = repository.like(postID)
    override fun onShareClick(post: Post) {
        sharePostContent.value = SingleEvent(post.content)
        repository.share(post)
    }
    override fun onRemoveClick(postID: Long) = repository.remove(postID)
    override fun onEditClick(post: Post) {
        currentPost.value = post
    }

    // endregion PostInteractionListener
}
