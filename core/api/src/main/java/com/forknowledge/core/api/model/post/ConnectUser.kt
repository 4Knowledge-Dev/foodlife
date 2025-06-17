package com.forknowledge.core.api.model.post

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class ConnectUser(val email: String)