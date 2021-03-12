package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.remote.responses.OrderResponse
import org.tridzen.mamafua.data.remote.responses.OrdersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OrdersApi {

    @GET("orders/find/{userId}")
    suspend fun fetchItems(@Query("userId") userId: String): OrdersResponse

    @GET("orders/{orderId}")
    suspend fun fetchOrder(@Query("orderId") orderId: String): OrderResponse
}