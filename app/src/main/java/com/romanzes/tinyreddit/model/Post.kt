package com.romanzes.tinyreddit.model

import com.romanzes.tinyreddit.common.Strings
import com.romanzes.tinyreddit.dto.Image

data class Post(
    val title: String,
    val author: String,
    val subreddit: String,
    val previewLink: String?,
    val link: String
)

class PostTransformer(private val strings: Strings) : (com.romanzes.tinyreddit.dto.Post) -> Post {
    override fun invoke(post: com.romanzes.tinyreddit.dto.Post): Post {
        return Post(
            title = post.title,
            author = strings.author(post.author),
            subreddit = strings.subreddit(post.subreddit),
            previewLink = post.preview?.images?.selectImage(),
            link = strings.postLink(post.permalink)
        )
    }

    private fun List<Image>.selectImage(): String? =
        this
            .map { it.source }
            .minBy { it.width * it.height }
            ?.url
}
