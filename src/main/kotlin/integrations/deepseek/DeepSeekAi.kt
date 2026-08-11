package com.speachr.integrations.deepseek

import com.speachr.config.DeepSeekConfig
import com.speachr.integrations.deepseek.dto.DeepSeekRequest
import com.speachr.integrations.deepseek.dto.DeepSeekResponse
import com.speachr.integrations.deepseek.dto.Message
import com.speachr.integrations.deepseek.dto.ResponseFormat
import com.speachr.integrations.deepseek.dto.Thinking
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeepSeekAi(private val config: DeepSeekConfig, private val httpClient: HttpClient) {

    suspend fun refineText(
        sourceText: String,
        targetLanguage: String = "EN",
        sourceLanguage: String = "EN"
    ): DeepSeekResponse {
        val SYSTEM_PROMPT = """
            You are a text refinement and translation assistant.

            The user provides:
            - source_language
            - target_language
            - input_text

            Your task is to transform the input into clear, natural, and polished text.

            Rules:
            - Preserve the exact meaning and intent of the input.
            - Remove filler words, verbal disfluencies, unnecessary repetitions, and speech artifacts such as "um", "uh", "ah", etc.
            - Correct grammar, spelling, punctuation, capitalization, and sentence structure.
            - Improve readability and fluency without adding information or changing the speaker's intent.
            - Preserve names, numbers, technical terms, and important details exactly.
            - If source_language differs from target_language, translate the refined text into target_language.
            - If they are the same, only refine the text.
            - Do not explain, summarize, or add commentary.
            - Return ONLY valid JSON.

            Output:
            {
              "text": "refined text"
            }
        """.trimIndent()
        val request = DeepSeekRequest(
            messages = listOf(
                Message(
                    role = "system", content = SYSTEM_PROMPT
                ), Message(
                    role = "user", content = """
                        {
                        source_language: $sourceLanguage
                        target_language: $targetLanguage
                        input_text: $sourceText
                        }
                    """.trimIndent()
                )
            ),
            model = "deepseek-v4-flash",
            thinking = Thinking(type = "disabled"),
            reasoning_effort = "low",
            max_tokens = 4096,
            response_format = ResponseFormat(type = "text"),
            temperature = 1.0,
            top_p = 1.0,
            tool_choice = "none",
            logprobs = false,
            stream = false
        )
        val response = httpClient.post("${config.apiUrl}/chat/completions") {
            header("Authorization", "Bearer ${config.apiKey}")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body<DeepSeekResponse>()
    }
}