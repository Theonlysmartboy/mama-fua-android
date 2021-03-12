package org.tridzen.mamafua.data.remote.responses

data class TokenResponse(
    val authToken: String?,
    val refreshToken: String?
)