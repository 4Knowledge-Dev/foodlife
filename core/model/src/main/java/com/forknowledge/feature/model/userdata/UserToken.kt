package com.forknowledge.feature.model.userdata

data class UserToken(
    val username: String,
    val hashKey: String
) {
    fun isValid() = username.isNotEmpty() && hashKey.isNotEmpty()
}
