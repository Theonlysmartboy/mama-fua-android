package org.tridzen.mamafua.data.remote.responses

data class TokenResponse(
    val access_token: String?,
    val refresh_token: String?
)