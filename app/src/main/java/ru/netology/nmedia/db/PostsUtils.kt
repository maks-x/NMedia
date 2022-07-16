package ru.netology.nmedia.db

import ru.netology.nmedia.objects.Post

internal fun Post.toPostEntity() = PostEntity(
    id = id,
    author = author,
    published = published,
    avatarID = avatarID,
    text = text,
    videoLink = videoLink,
    likedByMe = likedByMe,
    likesCount = likesCount,
    commentsCount = commentsCount,
    shareCount = shareCount,
    viewsCount = viewsCount
)

internal fun PostEntity.toPost() = Post(
    id = id,
    author = author,
    published = published,
    avatarID = avatarID,
    text = text,
    videoLink = videoLink,
    likedByMe = likedByMe,
    likesCount = likesCount,
    commentsCount = commentsCount,
    shareCount = shareCount,
    viewsCount = viewsCount
)