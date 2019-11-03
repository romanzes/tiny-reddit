package com.romanzes.tinyreddit.common

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SchedulersProvider {
    fun io(): Scheduler = Schedulers.io()
}
