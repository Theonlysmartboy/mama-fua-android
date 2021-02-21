package org.tridzen.mamafua.ui.home.order

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.remote.repository.OrdersRepository
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    suspend fun theOrders(id: String) =
        ordersRepository.getOrders(id)
}