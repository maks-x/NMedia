package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.fillWithSample

class PostRepositoryInMemoryImpl : PostRepository {

    override val data = MutableLiveData<List<Post>>().fillWithSample()

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    private var newPostID = posts.size + 1L

    override fun like(postID: Long) {
        data.value = posts.map { post ->
            if (post.id != postID) post
            else {
                val isLiked = !post.likedByMe
                val correction = if (isLiked) 1 else -1
                post.copy(
                    likedByMe = isLiked,
                    likesCount = post.likesCount + correction
                )
            }
        }
    }

    override fun share(post: Post) {
        data.value = posts.map {
            if (it != post) it
            else it.copy(
                shareCount = it.shareCount + 1
            )
        }
    }

    override fun remove(postID: Long) {
        data.value = posts.filterNot { it.id == postID }
    }

    override fun save(post: Post) =
        when (post.id) {
            Post.DEFAULT_POST_ID -> addNewPost(post)
            else -> updatePost(post)
        }

    private fun addNewPost(post: Post) {
        data.value = listOf(
            post.copy(
                id = newPostID,
                content = "Новость №$newPostID\n" + post.content
            )
        ) + posts
        newPostID++
    }

    private fun updatePost(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

}