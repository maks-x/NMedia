package ru.netology.nmedia.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.COMMON_SHARED_PREFS_KEY
import ru.netology.nmedia.utils.NEXT_POST_ID_PREFS_KEY
import ru.netology.nmedia.utils.POSTS_PREFS_KEY
import ru.netology.nmedia.utils.samplePosts

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        COMMON_SHARED_PREFS_KEY, Context.MODE_PRIVATE
    )

    override val data: MutableLiveData<List<Post>>

    init {
        val posts: List<Post> = prefs.getString(POSTS_PREFS_KEY, null)
            ?.let { serializedPosts ->
                Json.decodeFromString(serializedPosts)
            } ?: emptyList()
        data = MutableLiveData(samplePosts(application) + posts)
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    private var newPostID = prefs.getLong(NEXT_POST_ID_PREFS_KEY, posts.size + 1L)
        set(value) {
            prefs.edit { putLong(NEXT_POST_ID_PREFS_KEY, value) }
            field = value
        }

    override fun like(postID: Long) {
        posts = posts.map { post ->
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
        posts = posts.map {
            if (it != post) it
            else it.copy(
                shareCount = it.shareCount + 1
            )
        }
    }

    override fun remove(postID: Long) {
        posts = posts.filterNot { it.id == postID }
    }

    override fun save(post: Post) =
        when (post.id) {
            Post.DEFAULT_POST_ID -> addNewPost(post)
            else -> updatePost(post)
        }

    private fun addNewPost(post: Post) {
        posts = listOf(
            post.copy(
                id = newPostID,
                text = "Новость №$newPostID\n" + post.text
            )
        ) + posts
        newPostID += 1
    }

    private fun updatePost(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }
}