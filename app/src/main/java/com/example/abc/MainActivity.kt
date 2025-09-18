package com.example.abc

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

class MainActivity : ComponentActivity() {
    
    private lateinit var webView: WebView
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        webView = WebView(this)
        setContentView(webView)
        
        // WebView 설정
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
        
        // WebViewClient 설정
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        
        // JavaScript Interface 추가
        webView.addJavascriptInterface(WebInterface(this, webView), "AndroidInterface")
        
        // assets의 index.html 로드
        webView.loadUrl("file:///android_asset/index.html")
        
        // 뒤로가기 버튼 처리
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }
}