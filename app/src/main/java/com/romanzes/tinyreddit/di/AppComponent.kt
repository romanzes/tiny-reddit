package com.romanzes.tinyreddit.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.romanzes.tinyreddit.network.PostsClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

fun app(): AppComponent = AppModule

interface AppComponent {
    val postsClient: PostsClient
}

object AppModule : AppComponent {
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build()

    override val postsClient: PostsClient = retrofit.create(PostsClient::class.java)
}
