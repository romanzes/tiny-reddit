package com.romanzes.tinyreddit.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.romanzes.tinyreddit.R
import com.romanzes.tinyreddit.dto.Post

class PostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostViewHolder>() {
    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.apply {
            title.text = post.title
            author.text = post.author
            subreddit.text = post.subreddit
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
}
