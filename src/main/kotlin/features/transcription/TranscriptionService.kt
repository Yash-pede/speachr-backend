package com.speachr.features.transcription

import com.speachr.integrations.deepseek.DeepSeekAi
import com.speachr.integrations.groq.GroqClient
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

class TranscriptionService(private val groqClient: GroqClient, private val deepSeekAi: DeepSeekAi) {
    suspend fun transcribeAudio(
        audioFile: File, sourceLanguage: String = "EN", targetLanguage: String = "EN"
    ): String {
        val response = groqClient.transcribeAudioToText(
            audioFile = audioFile
        )
        val transcribedText = response["text"].toString()
        val refinedText =
            deepSeekAi.refineText(transcribedText, sourceLanguage, targetLanguage)
        return refinedText.choices
            .find { it.index == 0 }
            ?.message
            ?.content
            ?.let {
                Json.parseToJsonElement(it)
                    .jsonObject["text"]
                    ?.jsonPrimitive
                    ?.content
            }
            ?: ""
    }
}