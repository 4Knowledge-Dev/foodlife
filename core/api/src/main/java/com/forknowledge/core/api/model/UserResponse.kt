package com.forknowledge.core.api.model

import com.forknowledge.feature.model.userdata.UserToken
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class UserResponse(
    @SerialName("username")
    val username: String,
    @SerialName("hash")
    val hashKey: String
) {
    fun toUserToken() = UserToken(
        username = username,
        hashKey = hashKey
    )
}
