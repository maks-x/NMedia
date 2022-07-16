package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.objects.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    avatarID = getInt(getColumnIndexOrThrow(PostsTable.Column.AVATAR_ID.columnName)),
    text = getString(getColumnIndexOrThrow(PostsTable.Column.TEXT.columnName)),
    videoLink = getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_LINK.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    likesCount = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES_COUNT.columnName)),
    commentsCount = getInt(getColumnIndexOrThrow(PostsTable.Column.COMMENTS_COUNT.columnName)),
    shareCount = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARE_COUNT.columnName)),
    viewsCount = getInt(getColumnIndexOrThrow(PostsTable.Column.VIEWS_COUNT.columnName))
)