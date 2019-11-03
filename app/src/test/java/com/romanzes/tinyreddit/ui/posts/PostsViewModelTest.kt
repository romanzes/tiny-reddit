package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.common.SchedulersProvider
import com.romanzes.tinyreddit.common.Strings
import com.romanzes.tinyreddit.di.AppComponent
import com.romanzes.tinyreddit.dto.GetPostsResponse
import com.romanzes.tinyreddit.dto.PostData
import com.romanzes.tinyreddit.dto.PostsResponse
import com.romanzes.tinyreddit.model.PostTransformer
import com.romanzes.tinyreddit.network.PostsClient
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import com.romanzes.tinyreddit.dto.Post as PostDto

class PostsViewModelTest {
    private val mockPostsClient: PostsClient = mockk()
    private val mockPostTransformer: PostTransformer = mockk()
    private val mockStrings: Strings = mockk {
        every { generalErrorText() } returns GENERAL_ERROR
    }
    private val mockSchedulers: SchedulersProvider = mockk {
        every { io() } returns Schedulers.trampoline()
    }

    private val appComponent: AppComponent = mockk {
        every { postsClient } returns mockPostsClient
        every { postTransformer } returns mockPostTransformer
        every { strings } returns mockStrings
        every { schedulers } returns mockSchedulers
    }

    private val postsViewModel = PostsViewModel(appComponent)

    @Before
    fun setUp() {
        every { mockPostTransformer.invoke(any()) } answers {
            mockk {
                every { title } returns firstArg<PostDto>().title
            }
        }
    }

    @Test
    fun `error in client leads to error UI state`() {
        // given
        every { mockPostsClient.getAllPosts() } returns Observable.error(Exception())

        // when
        val test = postsViewModel.uiState().test()
        postsViewModel.onScreenLoaded()

        // then
        test.assertValues(
            PostsUiState.Loading,
            PostsUiState.Error(GENERAL_ERROR)
        )
    }

    @Test
    fun `posts are sorted by upvotes descending`() {
        // given
        val goodPostTitle = "Good Post"
        val badPostTitle = "Bad Post"
        val neutralPostTitle = "Neutral Post"
        every { mockPostsClient.getAllPosts() } returns Observable.just(
            responseDto(
                postDto(badPostTitle, -1),
                postDto(goodPostTitle, 5),
                postDto(neutralPostTitle, 0)
            )
        )

        // when
        val test = postsViewModel.uiState().test()
        postsViewModel.onScreenLoaded()

        // then
        test.assertValueAt(0, PostsUiState.Loading)
        val expectedTitles = listOf(goodPostTitle, neutralPostTitle, badPostTitle)
        test.assertValueAt(1) { uiState ->
            uiState is PostsUiState.Loaded && uiState.posts.map { it.title } == expectedTitles
        }
    }

    private fun responseDto(vararg posts: PostDto) = GetPostsResponse(
        posts = PostsResponse(posts = posts.map { PostData(it) })
    )

    private fun postDto(mockTitle: String, mockUps: Int): PostDto = mockk {
        every { title } returns mockTitle
        every { ups } returns mockUps
    }

    companion object {
        const val GENERAL_ERROR = "General Error"
    }
}
