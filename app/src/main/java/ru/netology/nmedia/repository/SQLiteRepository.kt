package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.samplePosts

class SQLiteRepository(
    private val dao: PostDao,
    context: Context
) : PostRepository {

    override val data = MutableLiveData<List<Post>>()

    override var postDraft by dao::draft

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    init {
        data.value = samplePosts(context, dao) + dao.getAll()
    }

    override fun like(postID: Long) {
        dao.likeByID(postID)
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
        dao.shareByID(post.id)
        data.value = posts.map {
            if (it.id != post.id) it
            else it.copy(
                shareCount = it.shareCount + 1
            )
        }
    }

    override fun remove(postID: Long) {
        dao.removeByID(postID)
        data.value = posts.filterNot { it.id == postID }
    }

    override fun save(post: Post) {
        val savedPost = dao.save(post)
        when (post.id) {
            Post.DEFAULT_POST_ID -> addNewPost(savedPost)
            else -> updatePost(savedPost)
        }
    }

    private fun addNewPost(post: Post) {
        data.value = listOf(post) + posts
    }

    private fun updatePost(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

}