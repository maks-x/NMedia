package ru.netology.nmedia

interface PostInteractionListener {
    fun onLikeClick(postID: Long)
    fun onShareClick(postID: Long)
    fun onRemoveClick(postID: Long)
    fun onEditClick(post: Post)
}