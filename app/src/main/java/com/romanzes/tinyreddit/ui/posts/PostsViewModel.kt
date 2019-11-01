package com.romanzes.tinyreddit.ui.posts

import com.romanzes.tinyreddit.di.app
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
    private val postsStatusSubject = BehaviorSubject.createDefault(false)

    private val disposables = CompositeDisposable()

    fun onScreenLoaded() {
        disposables += postsClient
            .getAllPosts()
            .subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { postsStatusSubject.onNext(true) })
    }

    fun progressBarVisible(): Observable<Boolean> = postsStatusSubject.map { !it }

    fun onDestroy() {
        disposables.clear()
    }
}
