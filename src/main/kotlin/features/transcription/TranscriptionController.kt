package com.speachr.features.transcription

import com.speachr.features.transcription.dto.TranscriptionResponse
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyTo
import java.io.File

class TranscriptionController(private val transcriptionService: TranscriptionService) {
    suspend fun transcribe(call: ApplicationCall) {

        var fileName = ""
        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    fileName = java.time.Instant.now().toString() + "_" + part.originalFileName as String
                    var file = File("uploads/$fileName")
                    part.provider().copyTo(file.writeChannel())
                }

                else -> {}
            }
            part.release()
        }

        val response = transcriptionService.transcribeAudio(File("uploads/$fileName"))

        call.respond(
            TranscriptionResponse(
                text = response,
                sourceLanguage = "EN",
            )
        )

    }
}