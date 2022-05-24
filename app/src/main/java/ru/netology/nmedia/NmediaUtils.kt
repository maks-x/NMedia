package ru.netology.nmedia

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.netology.nmedia.databinding.PostsFeedBinding
import kotlin.math.acos

internal fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /* flags = */0)
}

internal fun PostsFeedBinding.clearInputArea() {
    inputText.clearFocus()
    inputText.hideKeyboard()
    editingGroup.visibility = View.GONE
}

internal fun <T> List<T>.repeatIfOutOfBound(requiredIndex: Int) =
    this[requiredIndex % size]

class SingleEvent {
    private var hasBeenHandled = false

    fun runIfNotHandled(action: () -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            action()
        }
    }
}