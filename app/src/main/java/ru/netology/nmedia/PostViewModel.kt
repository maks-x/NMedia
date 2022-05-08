package ru.netology.nmedia

import androidx.lifecycle.ViewModel

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data
    fun likeByID(id: Long) = repository.like(id)
    fun shareByID(id: Long) = repository.share(id)
}