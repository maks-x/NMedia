package ru.netology.nmedia.service

internal enum class Action(
    val key: String
) {
    Like("LIKE"),
    NewPost("NEW_POST");

    companion object {
        const val KEY = "action"
    }
}