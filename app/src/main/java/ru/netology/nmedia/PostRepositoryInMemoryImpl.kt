package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {
    private val news = listOf(
        "28 мая: Ливерпуль и Реал Мадрид!\n    #MUSTSEE",
        "Обладателем лучшего показателя точности паса (98%) является " +
                "Александр Ерохин (Зенит)",
        "Лучший бомбардиром остаётся Карим Бензема (15 голов в 11 матчах)",
        "У Винисиуса Жуниора есть шанс стать лучшим ассистентом нынешнего розыгрыша " +
                "(2 передачи до первого результата)",
        "Совершив 52 сейва, Тибо Куртуа ушёл в отрыв по результативности среди вратарей"
    )

    override val data = MutableLiveData(
        List(news.size) { index ->
            val postID = index + 1L
            val emoji = String(Character.toChars(0x1F60A))
            Post(
                id = postID,
                avatarID = R.mipmap.ic_champ_league_logo,
                author = "UEFA Champ. League",
                published = "07.05.2022",
                content = "Новость №$postID\n(не по актуальности, но по порядку $emoji)\n"
                        + news[index],
                likesCount = 0,
                commentsCount = 0,
                shareCount = 0,
                viewsCount = 0,
                likedByMe = false
            )
        }
    )

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override fun like(postID: Long) {
        data.value = posts.map { post ->
            if (post.id != postID) post
            else {
                val isLiked = !post.likedByMe
                val correction = if (isLiked) 1 else -1
                post.copy(
                    likedByMe = isLiked,
                    likesCount = post.likesCount + correction
                )
            }
        }
    }

    override fun share(postID: Long) {
        data.value = posts.map { post ->
            if (post.id != postID) post
            else post.copy(
                shareCount = post.shareCount + 1
            )
        }
    }
}