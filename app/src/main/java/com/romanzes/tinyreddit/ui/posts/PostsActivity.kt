package com.romanzes.tinyreddit.ui.posts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.romanzes.tinyreddit.R
import com.romanzes.tinyreddit.di.app
import com.romanzes.tinyreddit.model.Post
import com.romanzes.tinyreddit.ui.post.PostActivity
import com.romanzes.tinyreddit.ext.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_posts.*

class PostsActivity : AppCompatActivity() {
    private lateinit var viewModel: PostsViewModel

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO use a [androidx.lifecycle.ViewModel] to store non-config state
        viewModel = lastCustomNonConfigurationInstance as? PostsViewModel
            ?: PostsViewModel(app(this)).also { it.onScreenLoaded() }

        setContentView(R.layout.activity_posts)

        initUi()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? = viewModel

    private fun initUi() {
        errorRetry.setOnClickListener { viewModel.onRetryClicked() }

        // subscribe to UI updates
        disposables += viewModel
            .uiState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = this::updateUi)
    }

    private fun updateUi(uiState: PostsUiState) {
        progress.show(uiState.progressVisible)

        posts.show(uiState.postsVisible)
        uiState.postsToDisplay?.let { postsToDisplay ->
            posts.adapter = PostsAdapter(postsToDisplay)
                .also { it.onItemClicked = this::openPost }
            posts.layoutManager = LinearLayoutManager(this)
        }

        error.show(uiState.error != null)
        errorText.text = uiState.error
    }

    private fun openPost(post: Post) {
        startActivity(
            Intent(this, PostActivity::class.java)
                .putExtra(PostActivity.EXTRA_URL, post.link)
        )
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        disposables.clear()
        super.onDestroy()
    }
}
