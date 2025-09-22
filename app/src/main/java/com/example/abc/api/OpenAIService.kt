package com.example.abc.api

import com.example.abc.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class OpenAIService {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = BuildConfig.OPENAI_API_KEY

    suspend fun getCompletion(userQuestion: String, profileContext: String = ""): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val systemPrompt = if (profileContext.isNotEmpty()) {
                    """
                    당신은 개인정보 검색 어시스턴트입니다. 
                    
                    사용자의 개인정보: $profileContext
                    
                    사용자가 자신에 대한 질문을 하면, 위의 개인정보에서 관련된 답변을 찾아서 정확하게 답해주세요.
                    
                    규칙:
                    1. 개인정보에서 직접적으로 찾을 수 있는 정보만 답변하세요
                    2. 개인정보에 없는 내용은 "제공된 정보에서 해당 내용을 찾을 수 없습니다"라고 답하세요
                    3. 간결하고 명확하게 답변하세요
                    4. 개인정보를 그대로 인용해서 답변하세요
                    """.trimIndent()
                } else {
                    "당신은 도움이 되는 AI 어시스턴트입니다."
                }

                val messages = listOf(
                    Message("system", systemPrompt),
                    Message("user", userQuestion)
                )

                val request = OpenAIRequest(
                    model = "gpt-3.5-turbo",
                    messages = messages,
                    max_tokens = 1000,
                    temperature = 0.7
                )

                val json = gson.toJson(request)
                val body = json.toRequestBody("application/json".toMediaType())

                val httpRequest = Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build()

                val response = client.newCall(httpRequest).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val openAIResponse = gson.fromJson(responseBody, OpenAIResponse::class.java)
                    val reply = openAIResponse.choices.firstOrNull()?.message?.content ?: "No response"
                    Result.success(reply)
                } else {
                    Result.failure(Exception("API Error: ${response.code} ${response.message}"))
                }
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}