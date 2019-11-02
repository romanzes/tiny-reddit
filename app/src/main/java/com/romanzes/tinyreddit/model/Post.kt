package com.romanzes.tinyreddit.model

import com.romanzes.tinyreddit.dto.Image
import com.romanzes.tinyreddit.util.Strings

data class Post(
    val title: String,
    val author: String,
    val subreddit: String,
    val previewLink: String?,
    val link: String
) {
    companion object {
        const val POST_LINK_TEMPLATE = "https://m.reddit.com%s"

        fun fromDto(
            post: com.romanzes.tinyreddit.dto.Post,
            strings: Strings
        ) = Post(
            title = post.title,
            author = strings.author(post.author),
            subreddit = strings.subreddit(post.subreddit),
            previewLink = post.preview?.images?.selectImage(),
            link = String.format(POST_LINK_TEMPLATE, post.permalink)
        )

        private fun List<Image>.selectImage(): String? =
            this
                .map { it.source }
                .minBy { it.width * it.height }
                ?.url
    }
}
