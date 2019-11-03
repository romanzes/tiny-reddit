package com.romanzes.tinyreddit.common

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * A wrapper for RxJava's schedulers. Use it instead of accessing [Schedulers] in a static way to
 * make the code testable.
 */
class SchedulersProvider {
    fun io(): Scheduler = Schedulers.io()
}
