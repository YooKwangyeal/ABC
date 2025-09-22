package com.example.abc

import android.content.Context
import android.content.SharedPreferences
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.abc.api.OpenAIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class WebInterface(private val context: Context, private val webView: WebView) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    
    private val client = OkHttpClient()
    private val openAIService = OpenAIService()
    
    @JavascriptInterface
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    
    @JavascriptInterface
    fun loadData(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
    
    @JavascriptInterface
    fun saveProfile(json: String) {
        sharedPreferences.edit().putString("user_profile", json).apply()
    }
    
    @JavascriptInterface
    fun getProfile(): String {
        return sharedPreferences.getString("user_profile", "") ?: ""
    }
    
    @JavascriptInterface
    fun askQuestion(question: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 저장된 프로필 가져오기
                val profileJson = sharedPreferences.getString("user_profile", "")
                
                if (profileJson.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript("showResponse('오류: 개인정보가 저장되지 않았습니다. 먼저 개인정보를 저장해주세요.')") { }
                    }
                    return@launch
                }
                
                // JSON에서 실제 개인정보 텍스트 추출
                val personalInfo = try {
                    val jsonData = JSONObject(profileJson)
                    jsonData.getString("personalInfo")
                } catch (e: Exception) {
                    // 기존 형식이거나 파싱 실패시 그대로 사용
                    profileJson
                }
                
                // OpenAI API 호출
                val result = openAIService.getCompletion(question, personalInfo)
                
                result.onSuccess { response ->
                    withContext(Dispatchers.Main) {
                        // JavaScript의 특수 문자 이스케이프 처리
                        val escapedResponse = response.replace("'", "\\'").replace("\n", "\\n").replace("\r", "")
                        webView.evaluateJavascript("showResponse('$escapedResponse')") { }
                    }
                }.onFailure { error ->
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript("showResponse('오류: ${error.message}')") { }
                    }
                }
                
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    webView.evaluateJavascript("showResponse('오류: ${e.message}')") { }
                }
            }
        }
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