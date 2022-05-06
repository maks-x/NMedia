package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {

    override val data = MutableLiveData(
        Post(
            avatarID = R.drawable.ic_soccer_48,
            author = "Про футбол",
            published = "07.05.2022",
            content = "«Фавориты в борьбе за «Золотой мяч» – это Садио Мане и Карим Бензема.»" +
                    ", - заявил легенда «Арсенала» Тьерри Анри.",
            likesCount = 1099,
            commentsCount = 4,
            shareCount = 0,
            viewsCount = 1999999999,
            likedByMe = false
        )
    )

    override fun like() {
        val post = data.notNullValue()
        val isLiked = !post.likedByMe
        val correction = if (isLiked) 1 else -1
        data.value = post.copy(
            likedByMe = isLiked,
            likesCount = post.likesCount + correction
        )
    }

    override fun share() {
        val post = data.notNullValue()
        data.value = post.copy(
            shareCount = post.shareCount + 1
        )
    }

    private fun <T> LiveData<T>.notNullValue(): T {
        return checkNotNull(this.value) {
            "Data value should not be null"
        }
    }
}