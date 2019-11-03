package com.romanzes.tinyreddit.model

import com.romanzes.tinyreddit.common.Strings
import com.romanzes.tinyreddit.dto.Image
import com.romanzes.tinyreddit.dto.Preview
import com.romanzes.tinyreddit.dto.Resolution
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import com.romanzes.tinyreddit.dto.Post as PostDto

class PostTransformerTest {
    private val strings: Strings = mockk {
        every { author(any()) } returns ""
        every { subreddit(any()) } returns ""
        every { postLink(any()) } returns ""
    }

    val postTransformer = PostTransformer(strings)

    @Test
    fun `title remains the same`() {
        // given
        val title = "An interesting title"
        val postDto = postDto(mockTitle = title)

        // when
        val post = postTransformer(postDto)

        // then
        assertEquals(title, post.title)
    }

    @Test
    fun `author, subreddit, and link go through strings`() {
        // given
        val author = "John Doe"
        val subreddit = "redditdev"
        val permalink = "/r/redditdev/test_post"
        val transformedAuthor = "By John Doe"
        val transformedSubreddit = "/r/redditdev"
        val transformedPermalink = "https://m.reddit.com/r/redditdev/test_post"
        val postDto = postDto(
            mockAuthor = author,
            mockSubreddit = subreddit,
            mockPermalink = permalink
        )
        every { strings.author(author) } returns transformedAuthor
        every { strings.subreddit(subreddit) } returns transformedSubreddit
        every { strings.postLink(permalink) } returns transformedPermalink

        // when
        val post = postTransformer(postDto)

        // then
        verify { strings.author(author) }
        verify { strings.subreddit(subreddit) }
        verify { strings.postLink(permalink) }
        assertEquals(transformedAuthor, post.author)
        assertEquals(transformedSubreddit, post.subreddit)
        assertEquals(transformedPermalink, post.link)
    }

    @Test
    fun `the smallest preview image is selected for preview link`() {
        // given
        val smallUrl = "small_url"
        val mediumUrl = "medium_url"
        val largeUrl = "large_url"
        val smallRes = Resolution(width = 100, height = 100, url = smallUrl)
        val mediumRes = Resolution(width = 200, height = 200, url = mediumUrl)
        val largeRes = Resolution(width = 300, height = 300, url = largeUrl)
        val mockImages = listOf(largeRes, smallRes, mediumRes)
            .map { mockk<Image> { every { source } returns it } }
        val preview: Preview = mockk {
            every { images } returns mockImages
        }
        val postDto = postDto(mockPreview = preview)

        // when
        val post = postTransformer(postDto)

        // then
        assertEquals(smallUrl, post.previewLink)
    }

    @Test
    fun `only area matters when choosing images`() {
        // given
        val smallUrl = "small_url"
        val largeUrl = "large_url"
        val smallRes = Resolution(width = 1000, height = 1, url = smallUrl)
        val largeRes = Resolution(width = 100, height = 100, url = largeUrl)
        val mockImages = listOf(smallRes, largeRes)
            .map { mockk<Image> { every { source } returns it } }
        val preview: Preview = mockk {
            every { images } returns mockImages
        }
        val postDto = postDto(mockPreview = preview)

        // when
        val post = postTransformer(postDto)

        // then
        assertEquals(smallUrl, post.previewLink)
    }

    @Test
    fun `no images leads to null preview link`() {
        // given
        val preview: Preview = mockk {
            every { images } returns listOf()
        }
        val postDto = postDto(mockPreview = preview)

        // when
        val post = postTransformer(postDto)

        // then
        assertEquals(null, post.previewLink)
    }

    private fun postDto(
        mockTitle: String = "",
        mockAuthor: String = "",
        mockSubreddit: String = "",
        mockPreview: Preview? = null,
        mockPermalink: String = ""
    ): PostDto = mockk {
        every { title } returns mockTitle
        every { author } returns mockAuthor
        every { subreddit } returns mockSubreddit
        every { preview } returns mockPreview
        every { permalink } returns mockPermalink
    }
}
