package com.romanzes.tinyreddit.ui.posts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.romanzes.tinyreddit.R
import com.romanzes.tinyreddit.network.PostsClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class PostsActivity : AppCompatActivity() {
    private val viewModel: PostsViewModel

    private lateinit var progress: View

    private val disposables = CompositeDisposable()

    init {
        val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        val postsClient = retrofit.create(PostsClient::class.java)
        viewModel = PostsViewModel(postsClient)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        progress = findViewById(R.id.progress)

        viewModel.onScreenLoaded()
        disposables += viewModel
            .progressBarVisible()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                progress.visibility = if (it) View.VISIBLE else View.GONE
            })
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        disposables.clear()
        super.onDestroy()
    }
}
