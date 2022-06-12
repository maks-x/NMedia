package ru.netology.nmedia.utils

class SingleEventWrapper<T>(private val content: T? = null) {

    private var hasBeenHandled = false

    fun runIfNotHandled(action: () -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            action()
        }
    }

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}