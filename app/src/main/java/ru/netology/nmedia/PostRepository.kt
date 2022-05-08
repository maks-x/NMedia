package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>
    fun like(postID: Long)
    fun share(postID: Long)
}

