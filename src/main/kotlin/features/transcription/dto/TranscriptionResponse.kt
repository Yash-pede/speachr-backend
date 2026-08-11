package com.speachr.features.transcription.dto

import kotlinx.serialization.Serializable

@Serializable
data class TranscriptionResponse(
    val text: String,
    val sourceLanguage: String,
)