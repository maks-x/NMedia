package ru.netology.nmedia.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.ui.MainActivity.Companion.IDENTIFIER_KEY

// region KEYBOARD

private val View.inputMethodManager
    get() = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

internal fun View.hideKeyboard() {
    inputMethodManager.hideSoftInputFromWindow(windowToken, /* flags = */0)
}

// клавиатура не подтягивается
// showSoftInput замылил... бестолку... Как сделать это правильно?
internal fun View.showKeyboard() {
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

//    а этот франкенштейн вообще не даёт клавиатуре пропасть
//    в режиме alwaysOnDisplay на моем Android 10
    //    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, /* flags = */0)
}

// endregion KEYBOARD

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

internal fun samplePosts(context: Context, dao: PostDao? = null): List<Post> {

    val prefs = context.getSharedPreferences(COMMON_SHARED_PREFS_KEY, Context.MODE_PRIVATE)

    if (!prefs.getBoolean(FIRST_START_PREFS_KEY, true))
        return emptyList()

    prefs.edit {
        putBoolean(FIRST_START_PREFS_KEY, false)
    }

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

    return List(startCount) { index ->
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
    }.reversed().also { dao?.saveAll(it) }
}

internal fun PostBinding.fillWithPost(post: Post) {
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

internal fun Fragment.sharePostWithIntent(content: String, intentIdentifier: String? = null) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
//        intentIdentifier.let { /*identifier*/ = it } выдаёт ошибку на  android < 29
        //пришлось положить строку
        putExtra(IDENTIFIER_KEY, intentIdentifier)

    }
    val shareIntent = Intent.createChooser(
        intent, getString(R.string.chooser_share_post)
    )
    startActivity(shareIntent)
}

// region APP_CONSTANTS

const val COMMON_SHARED_PREFS_KEY = "commonPrefs"
const val FIRST_START_PREFS_KEY = "firstStart"
const val NEXT_POST_ID_PREFS_KEY = "nextID"

const val POSTS_FILE_NAME = "posts.json"


// endregion APP_CONSTANTS