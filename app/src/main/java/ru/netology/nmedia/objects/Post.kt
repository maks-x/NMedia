package ru.netology.nmedia.objects

import kotlinx.serialization.Serializable
import ru.netology.nmedia.R

@Serializable
data class Post(
    val id: Long = DEFAULT_POST_ID,
    val author: String = "Who cares?",
    val published: String = "Whenever...",
    val avatarID: Int = R.drawable.ic_face_48dp,
    val text: String = EMPTY_POST_CONTENT,
    val videoLink: String? = null,
    val likedByMe: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val shareCount: Int = 0,
    val viewsCount: Int = 0
) {
    companion object {
        const val DEFAULT_POST_ID = 0L
        const val EMPTY_POST_CONTENT = ""
    }
}
