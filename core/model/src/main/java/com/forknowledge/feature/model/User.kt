package com.forknowledge.feature.model

data class User(
    val userId: String,
    val name: String,
    val imageUrl: String,
    val email: String = "",
)
