package org.tridzen.mamafua.ui.home.order.prepare.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.local.entities.Payment
import org.tridzen.mamafua.data.remote.repository.PaymentsRepository
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(private val paymentsRepository: PaymentsRepository) : ViewModel() {

    fun savePayment(payment: Payment) = viewModelScope.launch {
        paymentsRepository.saveProfile(payment)
    }

    fun deleteMode(id: String) = viewModelScope.launch(Dispatchers.IO) {
        paymentsRepository.deleteMode(id)
    }

    val payments = paymentsRepository.getAllPayments()

    fun editPayment(number: String, name: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        paymentsRepository.editMode(number, name, id)
    }

    fun makePayment(order: Order.Post) = viewModelScope.launch {
        paymentsRepository.makePayment(order)
    }
}