package org.tridzen.mamafua.ui.home.interfaces

import org.tridzen.mamafua.data.local.entities.Payment

interface OnPaymentListener {

    fun deletePayment(payment: Payment)
    fun editPayment(payment: Payment)
}