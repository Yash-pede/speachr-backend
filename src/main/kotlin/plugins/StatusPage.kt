package com.speachr.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respondText

fun Application.statusPage() {
    install(StatusPages) {

        exception<Throwable> { call, cause ->

            println("Error $cause")

            if (!call.response.isCommitted) {
                call.respondText(
                    text = "Internal Server Error",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}