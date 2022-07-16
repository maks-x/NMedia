package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.db.DraftEntity.Companion.DRAFT_PRIMARY_KEY_QUOTES

@Dao
interface PostDao {

    @Query(
        "SELECT draftText FROM draftTable WHERE draftKey = $DRAFT_PRIMARY_KEY_QUOTES"
    )
    fun getTextDraft(): String

    @Query(
        "REPLACE INTO draftTable (draftKey, draftText) VALUES ($DRAFT_PRIMARY_KEY_QUOTES, :draft)"
    )
    fun setTextDraft(draft: String?)


    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET text = :text WHERE id = :id")
    fun updateContentByID(id: Long, text: String)

    @Query(
        """
            UPDATE posts SET
            likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = :id
        """
    )
    fun likeByID(id: Long)

    @Query("UPDATE posts SET shareCount = shareCount + 1 WHERE id = :id")
    fun shareByID(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeByID(id: Long)
}