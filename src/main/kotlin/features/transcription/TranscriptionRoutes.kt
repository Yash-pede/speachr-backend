package com.speachr.features.transcription

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.transcriptionRoutes(controller: TranscriptionController) {
    route("/transcribe") {
        get {
            controller.transcribe(call)
        }
    }
}