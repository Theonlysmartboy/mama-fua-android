package org.tridzen.mamafua.data.remote.repository

import org.json.JSONObject
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.local.entities.Payment
import org.tridzen.mamafua.data.remote.network.current.apis.PurchaseApi

class PaymentsRepository(private val db: AppDatabase, private val api: PurchaseApi) :
    BaseRepository() {

    suspend fun saveProfile(payment: Payment) = db.getPaymentsDao().addPayment(payment)

    fun getAllPayments() = db.getPaymentsDao().getModes()

    suspend fun deleteMode(number: String) = db.getPaymentsDao().deleteMode(number)

    suspend fun editMode(number: String, name: String, id: Int) {
        db.getPaymentsDao().editMode(number, name, id)
    }

    suspend fun makePayment(order: Order) {
        val body = JSONObject()
        body.put("number", "254729226824")
        safeApiCall { api.makePayment(order) }
    }
}
