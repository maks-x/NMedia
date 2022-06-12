package ru.netology.nmedia.objects

import ru.netology.nmedia.R

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val avatarID: Int = R.drawable.ic_face_48dp,
    val content: String,
    val likedByMe: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val shareCount: Int = 0,
    val viewsCount: Int = 0
)