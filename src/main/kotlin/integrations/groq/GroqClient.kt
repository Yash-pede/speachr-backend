package com.speachr.integrations.groq

import com.speachr.config.GroqConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.JsonObject
import java.io.File

class GroqClient(
    private val config: GroqConfig,
    private val httpClient: HttpClient
) {
    suspend fun transcribeAudioToText(audioFile: File): JsonObject {
        val response = httpClient.submitFormWithBinaryData(
            url = config.apiUrl,
            formData = formData {
                append(
                    key = "file",
                    value = audioFile.readBytes(),
                    headers = Headers.build {

                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=\"${audioFile.name}\""
                        )

                        append(
                            HttpHeaders.ContentType,
                            "audio/wav"
                        )
                    }
                )
                append("model", "whisper-large-v3-turbo")
                append("temperature", "0")
                append("response_format", "verbose_json")
                append("language", "en")
            }
        ) {
            header(HttpHeaders.Authorization, "Bearer ${config.apiKey}")
        }

        println(response)
        println(response.bodyAsText())

        return response.body()
    }
}