package ru.netology.nmedia

data class Post(
    val author: String,
    val published: String,
    val avatarID: Int,
    val content: String,
    var likedByMe: Boolean = false,
    var likesCount: Int = 0,
    var commentsCount: Int = 0,
    var shareCount: Int = 0,
    var viewsCount: Int = 0
)
