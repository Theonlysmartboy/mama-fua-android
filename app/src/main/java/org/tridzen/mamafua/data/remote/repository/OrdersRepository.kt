package org.tridzen.mamafua.data.remote.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.ORDERS_SAVED_AT
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.network.current.apis.OrdersApi
import org.tridzen.mamafua.data.remote.responses.OrdersResponse
import org.tridzen.mamafua.utils.Constants.Companion.MINIMUM_INTERVAL_ORDERS
import org.tridzen.mamafua.utils.coroutines.Coroutines

class OrdersRepository(
    private val api: OrdersApi,
    private val db: AppDatabase,
    private val prefs: AppPreferences
) : BaseRepository() {

    private val orders = MutableLiveData<Resource<OrdersResponse>>()

    init {
        orders.observeForever {
            when (it) {
                is Resource.Success -> {
                    saveOrders(it.value.orders)
                }
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
            }
        }
    }

    private suspend fun fetchOrders(id: String) {
        val lastSavedAt = prefs.getValue(ORDERS_SAVED_AT)

        if (lastSavedAt.first() == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt.first()))) {
            try {
                val response = safeApiCall { api.fetchItems(id) }
                orders.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    suspend fun getOrders(id: String): LiveData<List<Order>> {
//        return withContext(Dispatchers.IO) {
//            fetchOrders(id)
//            db.getOrdersDao().getOrders()
//        }
//    }

    suspend fun getOrders(id: String) = safeApiCall {
        api.fetchItems(id)
    }

    private fun saveOrders(list: List<Order>) {
        Coroutines.io {
            prefs.saveValue(LocalDateTime.now().toString(), ORDERS_SAVED_AT)
            db.getOrdersDao().insertAll(list)
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.SECONDS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL_ORDERS
    }
}