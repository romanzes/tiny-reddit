package com.romanzes.tinyreddit.ui.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.romanzes.tinyreddit.R
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        intent.extras?.getString(EXTRA_URL)?.let { url ->
            webView.loadUrl(url)
        }
    }

    companion object {
        const val EXTRA_URL = "url"
    }
}
