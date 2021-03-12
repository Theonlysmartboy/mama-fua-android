package org.tridzen.mamafua.ui.home.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.repository.OrdersRepository
import org.tridzen.mamafua.data.remote.responses.OrdersResponse
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    private var _orders = MutableLiveData<Resource<OrdersResponse>>()
    val orders: LiveData<Resource<OrdersResponse>> get() = _orders

    suspend fun theOrders(id: String): LiveData<Resource<OrdersResponse>> {
        _orders.postValue(ordersRepository.getOrders(id))
        return orders
    }
}