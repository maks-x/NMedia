package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class PostEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "published")
    val published: String,

    @ColumnInfo(name = "avatarID")
    val avatarID: Int,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "videoLink")
    val videoLink: String?,

    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean,

    @ColumnInfo(name = "likesCount")
    val likesCount: Int,

    @ColumnInfo(name = "commentsCount")
    val commentsCount: Int,

    @ColumnInfo(name = "shareCount")
    val shareCount: Int,

    @ColumnInfo(name = "viewsCount")
    val viewsCount: Int
)
