package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Payload

data class PaymentResponse(
    val message: String,
    val payload: Payload
)