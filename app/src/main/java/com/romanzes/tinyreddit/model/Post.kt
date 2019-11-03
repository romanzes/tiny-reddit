package com.romanzes.tinyreddit.model

import com.romanzes.tinyreddit.common.Strings
import com.romanzes.tinyreddit.dto.Image
import com.romanzes.tinyreddit.dto.Post as PostDto

data class Post(
    val title: String,
    val author: String,
    val subreddit: String,
    val previewLink: String?,
    val link: String
)

class PostTransformer(private val strings: Strings) : (PostDto) -> Post {
    override fun invoke(post: PostDto): Post {
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
