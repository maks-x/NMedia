package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.objects.Post
import ru.netology.nmedia.utils.fillWithPost
import ru.netology.nmedia.utils.setBasicListeners
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

//        необходимо инициализировать значение post для использования
//        в стороннем коде (setBasicListeners)
        private var post = Post()

        init {
            with(postBinding) {
                setBasicListeners(post, listener)
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