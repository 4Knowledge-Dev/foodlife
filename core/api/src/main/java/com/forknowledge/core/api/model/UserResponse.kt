package com.forknowledge.core.api.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class UserResponse(@SerialName("hash") val hashKey: String)
