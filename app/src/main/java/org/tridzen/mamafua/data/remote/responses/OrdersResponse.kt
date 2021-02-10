package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Order

data class OrdersResponse(val message: String, val orders: List<Order>)
