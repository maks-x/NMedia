package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.fillWithPost
import ru.netology.nmedia.viewModel.PostInteractionListener

internal class PostsFeedAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsFeedAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val postBinding = PostBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(postBinding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(
        private val postBinding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(postBinding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, postBinding.postsOptions).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.popupRemove -> {
                            listener.onRemoveClick(post.id)
                            true
                        }
                        R.id.popupEdit -> {
                            listener.onEditClick(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            with(postBinding) {
                likes.setOnClickListener { listener.onLikeClick(post.id) }
                share.setOnClickListener { listener.onShareClick(post) }
                postsOptions.setOnClickListener { popupMenu.show() }
                videoPlay.setOnClickListener {
                    post.videoLink?.let { listener.onVideoLinkClick(it) }
                }
                videoPreview.setOnClickListener { videoPlay.performClick() }
                postNavigateArea.setOnClickListener { listener.onPostNavigateAreaClick(post) }
            }
        }

        fun bind(post: Post) {
            this.post = post
            postBinding.fillWithPost(post)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}