package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.di.app
import com.romanzes.tinyreddit.dto.PostData
import com.romanzes.tinyreddit.network.PostsClient
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PostsViewModel(
    private val postsClient: PostsClient = app().postsClient
) {
    private val uiStateSubject =
        BehaviorSubject.createDefault<PostsUiState>(PostsUiState.Loading)

    private val disposables = CompositeDisposable()

    fun onScreenLoaded() {
        disposables += postsClient
            .getAllPosts()
            .subscribeOn(Schedulers.io())
            .subscribeBy(onNext = {
                val posts = it.posts.posts.map(PostData::post)
                uiStateSubject.onNext(PostsUiState.Loaded(posts))
            })
    }

    fun uiState(): Observable<PostsUiState> = uiStateSubject

    fun onDestroy() {
        disposables.clear()
    }
}
