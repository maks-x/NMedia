package ru.netology.nmedia

interface PostInteractionListener {
    fun onLikeClick(postID: Long)
    fun onShareClick(post: Post)
    fun onRemoveClick(postID: Long)
    fun onEditClick(post: Post)
}