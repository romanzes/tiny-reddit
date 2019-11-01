package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.di.app
import com.romanzes.tinyreddit.dto.Post
import com.romanzes.tinyreddit.dto.PostData
import com.romanzes.tinyreddit.network.PostsClient
import com.romanzes.tinyreddit.util.Optional
import com.romanzes.tinyreddit.util.asOptional
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PostsViewModel(
    private val postsClient: PostsClient = app().postsClient
) {
    private val postsStatusSubject =
        BehaviorSubject.createDefault<PostsUiState>(PostsUiState.Loading)

    private val disposables = CompositeDisposable()

    fun onScreenLoaded() {
        disposables += postsClient
            .getAllPosts()
            .subscribeOn(Schedulers.io())
            .subscribeBy(onNext = {
                val posts = it.posts.posts.map(PostData::post)
                postsStatusSubject.onNext(PostsUiState.Loaded(posts))
            })
    }

    fun progressBarVisible(): Observable<Boolean> =
        postsStatusSubject.map { it == PostsUiState.Loading }

    fun posts(): Observable<Optional<List<Post>>> = postsStatusSubject.map {
        (it as? PostsUiState.Loaded)?.posts.asOptional()
    }

    fun onDestroy() {
        disposables.clear()
    }
}
