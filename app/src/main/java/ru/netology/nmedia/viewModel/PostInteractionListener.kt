package ru.netology.nmedia.viewModel

import ru.netology.nmedia.objects.Post

interface PostInteractionListener {
    fun onLikeClick(postID: Long)
    fun onShareClick(post: Post)
    fun onRemoveClick(postID: Long)
    fun onEditClick(post: Post)
    fun onVideoLinkClick(link: String)
}