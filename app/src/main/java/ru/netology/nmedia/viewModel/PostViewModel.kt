package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.repository.FilePostRepository
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.utils.SingleEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)
    val data by repository::data

    val navigateToPostContentActivityEvent = MutableLiveData<SingleEvent<Post>>()

    val navigateToPostFragmentEvent = MutableLiveData<SingleEvent<Post>>()

    val sharePostContentEvent = MutableLiveData<SingleEvent<String>>()

    val videoPlayEvent = MutableLiveData<SingleEvent<String>>()

    val postFragmentRemoveEvent = MutableLiveData<SingleEvent<Long>>()
    val postFragmentEditEvent = MutableLiveData<SingleEvent<Post>>()
    val postFragmentShareEvent = MutableLiveData<SingleEvent<String>>()


    val scrollOnNewPostEvent = MutableLiveData<SingleEvent<Unit>>()

    fun scrollOnTop() {
        scrollOnNewPostEvent.value = SingleEvent()
    }

    fun savePost(newOrEditedPost: Post) {
        repository.save(newOrEditedPost)
    }

    fun onAddButtonClick() {
        navigateToPostContentActivityEvent.value = SingleEvent(Post())
    }

    fun onPostFragmentRemove(postID: Long) {
        postFragmentRemoveEvent.value = SingleEvent(postID)
    }

    fun onPostFragmentEdit(post: Post) {
        postFragmentEditEvent.value = SingleEvent(post)
    }

    fun onPostFragmentShare(post: Post) {
        postFragmentShareEvent.value = SingleEvent(post.toString())
        repository.share(post)
    }

    // region PostInteractionListener

    override fun onLikeClick(postID: Long) = repository.like(postID)
    override fun onShareClick(post: Post) {
        sharePostContentEvent.value = SingleEvent(post.toString())
        repository.share(post)
    }

    override fun onRemoveClick(postID: Long) = repository.remove(postID)
    override fun onEditClick(post: Post) {
        navigateToPostContentActivityEvent.value = SingleEvent(post)
    }

    override fun onVideoLinkClick(link: String) {
        videoPlayEvent.value = SingleEvent(link)
    }

    override fun onPostNavigateAreaClick(post: Post) {
        navigateToPostFragmentEvent.value = SingleEvent(post)
    }


    // endregion PostInteractionListener
}
