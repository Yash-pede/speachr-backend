package com.speachr.plugins

import com.speachr.config.DeepSeekConfig
import com.speachr.config.GroqConfig
import com.speachr.features.transcription.TranscriptionController
import com.speachr.features.transcription.TranscriptionService
import com.speachr.integrations.deepseek.DeepSeekAi
import com.speachr.integrations.groq.GroqClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun Application.configureDependencies() {
    val appConfig = this.environment.config
    dependencies {

        provide<HttpClient> {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
        }

        provide<GroqConfig> {
            GroqConfig(
                apiKey = appConfig.property("groq.apiKey").getString(),
                apiUrl = appConfig.property("groq.apiUrl").getString()
            )
        }

        provide<GroqClient> {
            GroqClient(
                config = resolve(),
                httpClient = resolve(),
            )
        }

        provide<DeepSeekConfig> {
            DeepSeekConfig(
                apiKey = appConfig.property("deepseek.apiKey").getString(),
                apiUrl = appConfig.property("deepseek.apiUrl").getString()
            )
        }

        provide<DeepSeekAi> {
            DeepSeekAi(
                config = resolve(),
                httpClient = resolve()
            )
        }

        provide<TranscriptionService> {
            TranscriptionService(
                groqClient = resolve(),
                deepSeekAi = resolve()
            )
        }
        provide<TranscriptionController> {
            TranscriptionController(transcriptionService = resolve())
        }
    }
}