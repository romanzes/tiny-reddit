package com.romanzes.tinyreddit.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.romanzes.tinyreddit.R
import com.romanzes.tinyreddit.model.Post
import com.romanzes.tinyreddit.ext.show

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
    var onItemClicked: ((Post) -> Unit)? = null

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.apply {
            onItemClicked?.let { listener ->
                itemView.setOnClickListener {
                    listener(post)
                }
            }
            title.text = post.title
            author.text = post.author
            subreddit.text = post.subreddit
            preview.show(post.previewLink != null)
            post.previewLink?.let { link ->
                Glide
                    .with(preview.context)
                    .load(link)
                    .into(preview)
            }
        }
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
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author)
    val subreddit: TextView = itemView.findViewById(R.id.subreddit)
    val preview: ImageView = itemView.findViewById(R.id.preview)
}
