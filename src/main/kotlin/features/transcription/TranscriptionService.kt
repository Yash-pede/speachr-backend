package com.speachr.features.transcription

import com.speachr.errors.BadRequestException
import com.speachr.integrations.deepseek.DeepSeekAi
import com.speachr.integrations.groq.GroqClient
import com.speachr.plugins.AudioProcessor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

class TranscriptionService(
    private val groqClient: GroqClient,
    private val deepSeekAi: DeepSeekAi,
    private val ffmpeg: AudioProcessor
) {
    suspend fun transcribeAudio(
        audioFile: File, sourceLanguage: String = "EN", targetLanguage: String = "EN"
    ): String {

        val newFlacFile = File("uploads/${audioFile.nameWithoutExtension}.flac")
        ffmpeg.convert3gpToFlac(audioFile, newFlacFile)

        val response = groqClient.transcribeAudioToText(
            audioFile = newFlacFile
        )
        if (!response["error"]?.toString().isNullOrEmpty()) {
            throw BadRequestException("Error transcribing audio: ${response["error"]}")
        }

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