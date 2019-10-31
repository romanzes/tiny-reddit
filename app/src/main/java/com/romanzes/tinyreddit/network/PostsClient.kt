package com.romanzes.tinyreddit.network

import com.romanzes.tinyreddit.dto.GetPostsResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface PostsClient {
    @GET(".json")
    fun getAllPosts(): Observable<GetPostsResponse>
}
