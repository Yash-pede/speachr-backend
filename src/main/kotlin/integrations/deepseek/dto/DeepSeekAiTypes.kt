package com.speachr.integrations.deepseek.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DeepSeekRequest(
    val messages: List<Message>,
    val model: String,
    val thinking: Thinking,
    val reasoning_effort: String,
    val max_tokens: Int,
    val response_format: ResponseFormat,
    val stop: String? = null,
    val stream: Boolean,
    val stream_options: String? = null,
    val temperature: Double,
    val top_p: Double,
    val tools: String? = null,
    val tool_choice: String,
    val logprobs: Boolean,
    val top_logprobs: Int? = null
)

@Serializable
data class Message(
    val content: String,
    val role: String
)

@Serializable
data class Thinking(
    val type: String
)

@Serializable
data class ResponseFormat(
    val type: String
)



@Serializable
data class DeepSeekResponse(
    val id: String,
    @SerialName("object")
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    @SerialName("system_fingerprint")
    val systemFingerprint: String
)

@Serializable
data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: String? = null,
    @SerialName("finish_reason")
    val finishReason: String
)

@Serializable
data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Int,

    @SerialName("completion_tokens")
    val completionTokens: Int,

    @SerialName("total_tokens")
    val totalTokens: Int,

    @SerialName("prompt_tokens_details")
    val promptTokensDetails: PromptTokensDetails,

    @SerialName("prompt_cache_hit_tokens")
    val promptCacheHitTokens: Int,

    @SerialName("prompt_cache_miss_tokens")
    val promptCacheMissTokens: Int
)

@Serializable
data class PromptTokensDetails(
    @SerialName("cached_tokens")
    val cachedTokens: Int
)