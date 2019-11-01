package com.romanzes.tinyreddit.ui.posts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.romanzes.tinyreddit.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_posts.*

class PostsActivity : AppCompatActivity() {
    private val viewModel = PostsViewModel()

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        viewModel.onScreenLoaded()

        disposables += viewModel
            .uiState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = this::updateUi)
    }

    private fun updateUi(uiState: PostsUiState) {
        when (uiState) {
            is PostsUiState.Loading -> {
                progress.visibility = View.VISIBLE
                posts.visibility = View.GONE
            }
            is PostsUiState.Loaded -> {
                progress.visibility = View.GONE
                this.posts.apply {
                    visibility = View.VISIBLE
                    adapter = PostsAdapter(uiState.posts)
                    layoutManager = LinearLayoutManager(context);
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        disposables.clear()
        super.onDestroy()
    }
}
