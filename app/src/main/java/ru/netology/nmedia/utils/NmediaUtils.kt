package ru.netology.nmedia.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostsFeedBinding
import ru.netology.nmedia.objects.Post

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

internal fun Context.formatCountOf(property: Int): String {

    fun Int.roundTo(divisor: Int): Any = when (this % divisor >= divisor / 10) {
        true ->
            //не нашёл другого способа отбросить ненужные порядки без всякого округления
            "%.6f".format(this.toDouble() / divisor).dropLast(5)
        false ->
            this / divisor
    }

    val pattern: String

    return when (property) {
        0 -> ""
        in 0..999 -> {
            pattern = resources.getString(R.string.default_pattern)
            pattern.format(property)
        }
        in 1_000..999_999 -> {
            pattern = resources.getString(R.string.thousands_pattern)
            pattern.format(property.roundTo(1_000))
        }
        in 1_000_000..Int.MAX_VALUE -> {
            pattern = resources.getString(R.string.millions_pattern)
            pattern.format(property.roundTo(1_000_000))
        }
        else -> "err" //here could be custom exception
    }
}

internal fun MutableLiveData<List<Post>>.fillWithSample() = apply {
    val startCount = 100
    var newPostID = 1L

    val news = listOf(
        "28 мая: Ливерпуль и Реал Мадрид!\n    #MUSTSEE",

        "Обладателем лучшего показателя точности паса (98%) является " +
                "Александр Ерохин (Зенит)",

        "Лучший бомбардиром остаётся Карим Бензема (15 голов в 11 матчах)",

        "У Винисиуса Жуниора есть шанс стать лучшим ассистентом нынешнего розыгрыша " +
                "(2 передачи до первого результата)",

        "Совершив 52 сейва, Тибо Куртуа ушёл в отрыв по результативности среди вратарей"
    )

    val newsHeaderPattern = "Новость №%s\n"

    this.value = List(startCount) { index ->
        val postID = newPostID
        newPostID++
        Post(
            id = postID,
            avatarID = R.mipmap.ic_champ_league_logo,
            author = "UEFA Champ. League",
            published = "07.05.2022",
            content = newsHeaderPattern.format(postID)
                    + news.repeatIfOutOfBound(index),
            likesCount = 2000000000,
            commentsCount = 2000000000,
            viewsCount = 2000000000,
            shareCount = 2000000000
        )
    }.reversed()
}