package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.map
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toPost
import ru.netology.nmedia.db.toPostEntity
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.insertSamplePosts

class SQLiteRepository(
    private val dao: PostDao,
    contextForSample: Context
) : PostRepository {

    override val data = dao.getAll().map { postEntities ->
        postEntities.map { it.toPost() }
    }

    override var postDraft: String?
        get() = dao.getTextDraft()
        set(value) = dao.setTextDraft(value)

    init {
        insertSamplePosts(contextForSample, dao)
    }

    override fun save(post: Post) {
        post.toPostEntity().let {
            if (post.id == 0L) dao.insert(it) else dao.updateContentByID(it.id, it.text)
        }
    }

    override fun remove(postID: Long) {
        dao.removeByID(postID)
    }

    override fun like(postID: Long) {
        dao.likeByID(postID)
    }

    override fun share(post: Post) {
        dao.shareByID(post.id)
    }
}