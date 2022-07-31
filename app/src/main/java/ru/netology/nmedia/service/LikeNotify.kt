package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class LikeNotify(
    @SerializedName("userID")
    val userID: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postID")
    val postID: Long,

    @SerializedName("postAuthor")
    val postAuthor: String
) {
}