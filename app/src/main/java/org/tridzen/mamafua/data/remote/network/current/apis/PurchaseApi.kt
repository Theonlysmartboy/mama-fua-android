package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.remote.responses.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseApi {

    @POST("pay/")
    suspend fun makePayment(@Body orders: Order): PaymentResponse
}