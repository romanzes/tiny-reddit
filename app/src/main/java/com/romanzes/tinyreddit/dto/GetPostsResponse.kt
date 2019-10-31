package com.romanzes.tinyreddit.dto


import com.fasterxml.jackson.annotation.JsonProperty

data class GetPostsResponse(
    @JsonProperty("data")
    val posts: PostsResponse
)

data class PostsResponse(
    @JsonProperty("posts")
    val posts: List<PostData>
)

data class PostData(
    @JsonProperty("data")
    val post: Post
)

data class Post(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("author")
    val author: String,
    @JsonProperty("permalink")
    val permalink: String,
    @JsonProperty("subreddit")
    val subreddit: String,
    @JsonProperty("downs")
    val downs: Int,
    @JsonProperty("ups")
    val ups: Int,
    @JsonProperty("preview")
    val preview: Preview
)

data class Preview(
    @JsonProperty("enabled")
    val enabled: Boolean,
    @JsonProperty("images")
    val images: List<Image>
)

data class Image(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("resolutions")
    val resolutions: List<Resolution>
)

data class Resolution(
    @JsonProperty("height")
    val height: Int,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("width")
    val width: Int
)
