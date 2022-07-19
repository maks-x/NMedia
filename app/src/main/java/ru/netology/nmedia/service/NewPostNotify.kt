package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class NewPostNotify(
    @SerializedName("userID")
    val userID: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postID")
    val postID: Long,

    @SerializedName("text")
    val text: String
) {
}