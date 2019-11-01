package com.romanzes.tinyreddit.util

import android.content.Context
import com.romanzes.tinyreddit.R

class Strings(val context: Context) {
    fun getErrorText() = context.getString(R.string.general_error)
}
