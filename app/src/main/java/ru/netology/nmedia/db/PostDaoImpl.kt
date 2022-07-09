package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.objects.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {
    override fun getAll() =
        db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            null, null, null, null,
            "${PostsTable.Column.ID.columnName} DESC"
        ).use { cursor ->
            List(cursor.count) {
                cursor.moveToNext()
                cursor.toPost()
            }
        }

    override fun saveAll(posts: List<Post>) {
        posts.forEach { post ->
            val values = ContentValues().apply {
                put(PostsTable.Column.ID.columnName, post.id)
                put(PostsTable.Column.AUTHOR.columnName, post.author)
                put(PostsTable.Column.PUBLISHED.columnName, post.published)
                put(PostsTable.Column.AVATAR_ID.columnName, post.avatarID)
                put(PostsTable.Column.TEXT.columnName, post.text)
                put(PostsTable.Column.VIDEO_LINK.columnName, post.videoLink)
                put(PostsTable.Column.LIKED_BY_ME.columnName, post.likedByMe)
                put(PostsTable.Column.LIKES_COUNT.columnName, post.likesCount)
                put(PostsTable.Column.COMMENTS_COUNT.columnName, post.commentsCount)
                put(PostsTable.Column.SHARE_COUNT.columnName, post.shareCount)
                put(PostsTable.Column.VIEWS_COUNT.columnName, post.viewsCount)
            }
            db.insert(PostsTable.NAME, null, values)
        }
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.AVATAR_ID.columnName, post.avatarID)
            put(PostsTable.Column.TEXT.columnName, post.text)
            put(PostsTable.Column.VIDEO_LINK.columnName, post.videoLink)
        }

        val id = if (post.id != 0L) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else { // post.id == 0L
            db.insert(PostsTable.NAME, null, values)
        }

        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likeByID(id: Long) {
        val likesCount = PostsTable.Column.LIKES_COUNT.columnName
        val likedByMe = PostsTable.Column.LIKED_BY_ME.columnName
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                    $likesCount = $likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    $likedByMe = CASE WHEN $likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareByID(id: Long) {
        val shareCount = PostsTable.Column.SHARE_COUNT.columnName
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                    $shareCount = $shareCount + 1
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeByID(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }
}