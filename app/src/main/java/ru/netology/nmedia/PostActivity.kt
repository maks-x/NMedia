package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostLayoutBinding


class PostActivity : AppCompatActivity(R.layout.post_layout) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            avatarID = R.drawable.ic_soccer_48,
            author = "Про футбол",
            published = "30.04.2022",
            content = "«Барса», вероятно, покинет «Камп Ноу» в сезоне 2023/24.",
            likesCount = 1099,
            commentsCount = 4,
            shareCount = 0,
            viewsCount = 1999999999,
            likedByMe = false
        )
        binding.fill(post)

        val likeButton = findViewById<ImageButton>(R.id.likes_image)
        likeButton.setOnClickListener {
            binding.updateLikes(post)
        }

        val shareButton = findViewById<ImageButton>(R.id.share_image)
        shareButton.setOnClickListener {
            binding.updateShare(post)
        }
    }

    private fun PostLayoutBinding.fill(post: Post) {
        avatar.setImageResource(post.avatarID)
        setLikesImg(post.likedByMe)
        likesCount.text = formatCountOf(post.likesCount)
        commentsCount.text = formatCountOf(post.commentsCount)
        shareCount.text = formatCountOf(post.shareCount)
        viewsCount.text = formatCountOf(post.viewsCount)
        author.text = post.author
        content.text = post.content
        published.text = post.published
    }

    private fun PostLayoutBinding.setLikesImg(likedByMe: Boolean) {
        val imgResID = when (likedByMe) {
            true -> R.drawable.ic_favorite_clicked_24
            false -> R.drawable.ic_favorite_24
        }
        likesImage.setImageResource(imgResID)
    }

    private fun PostLayoutBinding.updateLikes(post: Post) {
        post.likedByMe = !post.likedByMe
        val correction =  if (post.likedByMe) 1 else -1
        post.likesCount += correction
        setLikesImg(post.likedByMe)
        likesCount.text = formatCountOf(post.likesCount)
    }

    private fun PostLayoutBinding.updateShare(post: Post) {
        post.shareCount++
        shareCount.text = formatCountOf(post.shareCount)
    }

    private fun formatCountOf(property: Int): String {

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
}