package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.objects.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    var postDraft: String?
    fun like(postID: Long)
    fun share(post: Post)
    fun remove(postID: Long)
    fun save(post: Post)

}

