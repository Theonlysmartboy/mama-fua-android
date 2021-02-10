package org.tridzen.mamafua.data.local.entities

data class Login(private val email: String, private val password: String)
data class SignUp(
    private val email: String,
    private val password: String,
    private val username: String
)