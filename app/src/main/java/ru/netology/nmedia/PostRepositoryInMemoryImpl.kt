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
    private val emoji = String(Character.toChars(0x1F60A))
    private val newsHeaderPattern = "Новость №%s\n(не по актуальности, но по порядку $emoji)\n"
    private fun <T> List<T>.repeatIfOutOfBound(requiredIndex: Int) =
        this[requiredIndex % size]

    companion object{
        private const val START_COUNT = 100
    }

    private var newPostID = 1L

    override val data = MutableLiveData(

        List(START_COUNT) { index ->
            val postID = newPostID
            newPostID++
            Post(
                id = postID,
                avatarID = R.mipmap.ic_champ_league_logo,
                author = "UEFA Champ. League",
                published = "07.05.2022",
                content = newsHeaderPattern.format(postID)
                        + news.repeatIfOutOfBound(index)
            )
        }.reversed()
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

    override fun remove(postID: Long) {
        data.value = posts.filterNot { it.id == postID }
    }

    override fun save(post: Post) =
        when (post.id) {
            PostRepository.NEW_POST_ID_CHECKER -> addNewPost(post)
            else -> updatePost(post)
        }

    private fun addNewPost(post: Post) {
        data.value = listOf(
            post.copy(
                id = newPostID,
                content = "Новость №$newPostID\n" + post.content
            )
        ) + posts
        newPostID++
    }

    private fun updatePost(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

}