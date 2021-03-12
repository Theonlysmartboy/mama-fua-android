package org.tridzen.mamafua.data.local.entities

data class Referral(
    val __v: Int,
    val _id: String,
    val code: String,
    val createdAt: String,
    val discount: Int,
    val referrer: String,
    val updatedAt: String,
    val validFor: String
)