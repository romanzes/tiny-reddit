package com.romanzes.tinyreddit.model

import com.romanzes.tinyreddit.util.Strings

data class Post(
    val title: String,
    val author: String,
    val subreddit: String
) {
    companion object {
        fun fromDto(
            post: com.romanzes.tinyreddit.dto.Post,
            strings: Strings
        ): Post {
            return Post(
                title = post.title,
                author = strings.author(post.author),
                subreddit = strings.subreddit(post.subreddit)
            )
        }
    }
}
