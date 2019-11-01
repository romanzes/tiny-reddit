package com.romanzes.tinyreddit.ui.posts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.romanzes.tinyreddit.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class PostsActivity : AppCompatActivity() {
    private val viewModel: PostsViewModel = PostsViewModel()

    private lateinit var progress: View

    private val disposables = CompositeDisposable()

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
