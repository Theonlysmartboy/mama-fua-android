package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.User

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User
)