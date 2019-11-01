package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.dto.Post

sealed class PostsUiState {
    object Loading : PostsUiState()
    data class Loaded(val posts: List<Post>) : PostsUiState()
}
