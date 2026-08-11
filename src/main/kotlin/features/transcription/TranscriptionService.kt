package com.speachr.features.transcription

import com.speachr.integrations.groq.GroqClient
import kotlinx.coroutines.delay
import java.io.File

class TranscriptionService(private val groqClient: GroqClient) {
    suspend fun transcribeAudio(
        audioFile: File, sourceLanguage: String = "EN", targetLanguage: String = "EN"
    ): String {
        val response = groqClient.transcribeAudioToText(
            audioFile = audioFile
        )
        return response["text"].toString()
    }
}