package com.speachr.plugins

import com.speachr.errors.InvalidInputException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respond

fun Application.statusPage() {
    install(StatusPages) {

        exception<Throwable> { call, cause ->

//            println("Error $cause")
            exception<NotFoundException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = mapOf("error" to cause.message)
                )
            }
            exception<BadRequestException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("error" to cause.message)
                )
            }

            exception<InvalidInputException> { call, cause ->
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("error" to cause.message)
                )
            }

            exception<Throwable> { call, cause ->
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = mapOf("error" to "An unexpected error occurred: ${cause.localizedMessage}")
                )
            }
        }
    }
}