package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.di.AppComponent
import com.romanzes.tinyreddit.dto.PostData
import com.romanzes.tinyreddit.model.Post
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PostsViewModel(appComponent: AppComponent) {
    private val postsClient = appComponent.postsClient
    private val strings = appComponent.strings

    private val uiStateSubject = BehaviorSubject.create<PostsUiState>()

    private val disposables = CompositeDisposable()

    fun onScreenLoaded() = loadPosts()

    fun onRetryClicked() = loadPosts()

    private fun loadPosts() {
        uiStateSubject.onNext(PostsUiState.Loading)
        disposables += postsClient
            .getAllPosts()
            .subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { response ->
                val posts = response.posts.posts
                    .map(PostData::post)
                    .map { Post.fromDto(it, strings) }
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
