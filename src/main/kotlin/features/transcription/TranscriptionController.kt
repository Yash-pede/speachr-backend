package com.speachr.features.transcription

import com.speachr.features.transcription.dto.TranscriptionResponse
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import java.io.File

class TranscriptionController(private val transcriptionService: TranscriptionService) {
    suspend fun transcribe(call: ApplicationCall) {

        val response = transcriptionService.transcribeAudio(File("src/harvard.wav"))
        call.respond(
            TranscriptionResponse(
                text = response,
                sourceLanguage = "EN",
            )
        )

    }
}