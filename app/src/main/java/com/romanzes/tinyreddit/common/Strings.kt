package com.romanzes.tinyreddit.common

import android.content.Context
import com.romanzes.tinyreddit.R

class Strings(val context: Context) {
    fun generalErrorText() = context.getString(R.string.general_error)

    fun author(author: String) = context.getString(R.string.author_pattern, author)

    fun subreddit(subreddit: String) =
        context.getString(R.string.subreddit_pattern, subreddit)
}
