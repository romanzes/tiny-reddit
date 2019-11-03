package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.model.Post

/**
 * A representation of PostsActivity UI at some point in time.
 */
sealed class PostsUiState {
    object Loading : PostsUiState()
    data class Loaded(val posts: List<Post>) : PostsUiState()
    data class Error(val text: String) : PostsUiState()

    val progressVisible: Boolean
        get() = this is Loading

    val postsVisible: Boolean
        get() = this is Loaded

    val error: String?
        get() = (this as? Error)?.text

    val postsToDisplay: List<Post>?
        get() = (this as? Loaded)?.posts
}
