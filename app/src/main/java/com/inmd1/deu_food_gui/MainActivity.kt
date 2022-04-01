package com.inmd1.deu_food_gui

import android.os.Bundle
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.inmd1.deu_food_gui.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = findViewById(R.id.webview)

        webView.apply {
            webViewClient = WebViewClient()

            settings.javaScriptEnabled = true
            settings.setSupportMultipleWindows(false)
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
        }
        val uri = "https://mobile.udon.party/"
        webView.loadUrl(uri)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }
}
