package com.example.abc

import android.content.Context
import android.content.SharedPreferences
import android.webkit.JavascriptInterface
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class WebInterface(private val context: Context, private val webView: WebView) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    
    private val client = OkHttpClient()
    
    @JavascriptInterface
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    
    @JavascriptInterface
    fun loadData(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
    
    @JavascriptInterface
    fun postRequest(url: String, jsonData: String, callbackFunction: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonData.toRequestBody(mediaType)
                
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()
                
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string() ?: ""
                    
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript("$callbackFunction('$responseBody')") { }
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    webView.evaluateJavascript("$callbackFunction('Error: ${e.message}')") { }
                }
            }
        }
    }
    
    @JavascriptInterface
    fun getRequest(url: String, callbackFunction: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string() ?: ""
                    
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript("$callbackFunction('$responseBody')") { }
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    webView.evaluateJavascript("$callbackFunction('Error: ${e.message}')") { }
                }
            }
        }
    }
}
