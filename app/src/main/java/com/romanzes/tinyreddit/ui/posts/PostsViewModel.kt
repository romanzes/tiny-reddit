package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.di.AppComponent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject

class PostsViewModel(appComponent: AppComponent) {
    private val postsClient = appComponent.postsClient
    private val postTransformer = appComponent.postTransformer
    private val strings = appComponent.strings
    private val schedulers = appComponent.schedulers

    private val uiStateSubject = BehaviorSubject.create<PostsUiState>()

    private val disposables = CompositeDisposable()

    fun onScreenLoaded() = loadPosts()

    fun onRetryClicked() = loadPosts()

    private fun loadPosts() {
        uiStateSubject.onNext(PostsUiState.Loading)
        disposables += postsClient
            .getAllPosts()
            .subscribeOn(schedulers.io())
            .subscribeBy(onNext = { response ->
                val posts = response.posts.posts
                    .map { it.post }
                    .sortedByDescending { it.ups } // most upvoted posts should be at the top
                    .map(postTransformer)
                uiStateSubject.onNext(PostsUiState.Loaded(posts))
            }, onError = {
                uiStateSubject.onNext(PostsUiState.Error(strings.generalErrorText()))
            })
    }

    fun uiState(): Observable<PostsUiState> = uiStateSubject

    fun onDestroy() {
        disposables.clear()
    }
}
