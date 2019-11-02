package com.romanzes.tinyreddit.common

import android.content.Context
import com.romanzes.tinyreddit.R

class Strings(private val context: Context) {
    fun generalErrorText(): String = context.getString(R.string.general_error)

    fun author(author: String): String = context.getString(R.string.author_pattern, author)

    fun subreddit(subreddit: String): String =
        context.getString(R.string.subreddit_pattern, subreddit)
}
