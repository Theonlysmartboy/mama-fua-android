package org.tridzen.mamafua.data.local.entities

data class OrderX(
    val __v: Int,
    val _id: String,
    val amount: Int,
    val center: String,
    val checkoutRequestId: String,
    val createdAt: String,
    val executionDate: String,
    val latitude: Double,
    val longitude: Double,
    val merchantRequestId: String,
    val paid: String,
    val paidVia: String,
    val phone: String,
    val placedBy: String,
    val profileId: String,
    val profileName: String,
    val services: List<String>,
    val status: String,
    val transactionId: String,
    val updatedAt: String
)