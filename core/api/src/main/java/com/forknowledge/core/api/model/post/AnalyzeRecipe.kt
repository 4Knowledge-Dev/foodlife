package com.forknowledge.core.api.model.post

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeRecipe(
    val title: String,
    val ingredients: List<String>,
    val instructions: List<String>
)
