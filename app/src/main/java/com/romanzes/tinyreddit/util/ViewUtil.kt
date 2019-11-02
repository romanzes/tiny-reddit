package com.romanzes.tinyreddit.util

import android.view.View

fun View.show(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
