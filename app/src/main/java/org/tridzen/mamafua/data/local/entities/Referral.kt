package org.tridzen.mamafua.data.local.entities

data class Referral(
    val _id: String,
    val referrer: String,
    val usedBy: List<String>,
    val amount: Int,
    val percent: Double
)