package com.speachr.features.transcription

import io.ktor.server.routing.Route
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.transcriptionRoutes(controller: TranscriptionController) {
    route("/audio/transcribe") {
        post {
            controller.transcribe(call)
        }
    }
}