package com.example.abc.api

data class Message(
    val role: String,
    val content: String
)

data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int? = null,
    val temperature: Double? = null
)

data class Choice(
    val message: Message,
    val finish_reason: String?
)

data class OpenAIResponse(
    val choices: List<Choice>
)