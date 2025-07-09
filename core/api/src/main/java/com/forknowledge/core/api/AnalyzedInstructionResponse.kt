package com.forknowledge.core.api

import com.forknowledge.core.api.model.InstructionResponse
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class AnalyzedInstructionResponse(
    @SerialName("parsedInstructions")
    val instruction: List<InstructionResponse>
)