package org.tridzen.mamafua.data.local.entities

data class Payload(
    val CheckoutRequestID: String,
    val CustomerMessage: String,
    val MerchantRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String
)