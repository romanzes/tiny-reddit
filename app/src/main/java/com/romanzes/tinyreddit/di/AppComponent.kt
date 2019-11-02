package com.romanzes.tinyreddit.di

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.romanzes.tinyreddit.model.PostTransformer
import com.romanzes.tinyreddit.network.PostsClient
import com.romanzes.tinyreddit.common.Strings
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

fun app(context: Context): AppComponent = AppModule(context)

interface AppComponent {
    val context: Context
    val postsClient: PostsClient
    val postTransformer: PostTransformer
    val strings: Strings
}

private class AppModule(override val context: Context) : AppComponent {
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build()

    override val postsClient: PostsClient = retrofit.create(PostsClient::class.java)

    override val strings = Strings(context)

    override val postTransformer = PostTransformer(strings)
}
