package com.romanzes.tinyreddit.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.romanzes.tinyreddit.R
import com.romanzes.tinyreddit.ext.show
import com.romanzes.tinyreddit.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
    var onItemClicked: ((Post) -> Unit)? = null

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onItemClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_post,
                parent,
                false
            )
        )
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(post: Post, onItemClicked: ((Post) -> Unit)?) {
        onItemClicked?.let { listener ->
            itemView.setOnClickListener {
                listener(post)
            }
        }
        itemView.title.text = post.title
        itemView.author.text = post.author
        itemView.subreddit.text = post.subreddit
        itemView.preview.show(post.previewLink != null)
        post.previewLink?.let { link ->
            Glide
                .with(itemView.context)
                .load(link)
                .into(itemView.preview)
        }
    }
}
