package ru.netology.nmedia

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostBinding

typealias clickOnPost = (Long) -> Unit

internal class PostsFeedAdapter(
    private val context: Context,
    private val likeByID: clickOnPost,
    private val shareByID: clickOnPost,
) : ListAdapter<Post, PostsFeedAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = PostBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    inner class ViewHolder(
        private val postBinding: PostBinding
    ) : RecyclerView.ViewHolder(postBinding.root) {

        private lateinit var post: Post

        init {
            postBinding.likesButton.setOnClickListener { likeByID(post.id) }
            postBinding.shareButton.setOnClickListener { shareByID(post.id) }
        }

        fun bind(post: Post) {
            this.post = post
            with(postBinding) {
                avatar.setImageResource(post.avatarID)
                selectLikesButtonImg(post.likedByMe)
                likesCount.text = formatCountOf(post.likesCount)
                commentsCount.text = formatCountOf(post.commentsCount)
                shareCount.text = formatCountOf(post.shareCount)
                viewsCount.text = formatCountOf(post.viewsCount)
                author.text = post.author
                content.text = post.content
                published.text = post.published
            }
        }

        private fun PostBinding.selectLikesButtonImg(likedByMe: Boolean) {
            val imgResID = when (likedByMe) {
                true -> R.drawable.ic_favorite_clicked_24
                false -> R.drawable.ic_favorite_24
            }
            likesButton.setImageResource(imgResID)
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
                    pattern = context.resources.getString(R.string.default_pattern)
                    pattern.format(property)
                }
                in 1_000..999_999 -> {
                    pattern = context.resources.getString(R.string.thousands_pattern)
                    pattern.format(property.roundTo(1_000))
                }
                in 1_000_000..Int.MAX_VALUE -> {
                    pattern = context.resources.getString(R.string.millions_pattern)
                    pattern.format(property.roundTo(1_000_000))
                }
                else -> "err" //here could be custom exception
            }
        }

    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}