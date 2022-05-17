package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val avatarID: Int = R.drawable.ic_post_add_48,
    val content: String,
    val likedByMe: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val shareCount: Int = 0,
    val viewsCount: Int = 0
)
