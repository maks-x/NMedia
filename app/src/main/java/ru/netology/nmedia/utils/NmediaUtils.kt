package ru.netology.nmedia.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.objects.Post

internal fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /* flags = */0)
}

//иначе клавиатура не подтягивается после requestFocus(), есть ли другой способ?
internal fun Activity.showKeyboard() {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
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
            text = newsHeaderPattern.format(postID)
                    + news.repeatIfOutOfBound(index),
            videoLink = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            likesCount = 2000000000,
            commentsCount = 2000000000,
            viewsCount = 2000000000,
            shareCount = 2000000000
        )
    }.reversed()
}

internal fun PostBinding.fillWithPost(post: Post?) {
    post?.let {
        avatar.setImageResource(post.avatarID)
        author.text = post.author
        content.text = post.text
        published.text = post.published
        likes.isChecked = post.likedByMe
        videoViewGroup.visibility =
            if (post.videoLink.isNullOrBlank()) View.GONE
            else View.VISIBLE


        with(root.context) {
            likes.text = formatCountOf(post.likesCount)
            comments.text = formatCountOf(post.commentsCount)
            share.text = formatCountOf(post.shareCount)
            views.text = formatCountOf(post.viewsCount)
        }
    }
}

internal fun Bundle.withPostContent(post: Post): Bundle {
    putString(POST_CONTENT_TEXT, post.text)
    putString(POST_CONTENT_VIDEO_LINK, post.videoLink)
    return this
}

// region APP_CONSTANTS
const val POST_CONTENT_TEXT = "postContentText"
const val POST_CONTENT_VIDEO_LINK = "postContentVideoLink"


// endregion APP_CONSTANTS