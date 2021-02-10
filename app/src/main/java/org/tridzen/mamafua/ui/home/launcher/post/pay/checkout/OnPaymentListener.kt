package org.tridzen.mamafua.ui.home.launcher.post.pay.checkout

import org.tridzen.mamafua.data.local.entities.Payment

interface OnPaymentListener {

    fun deletePayment(payment: Payment)
    fun editPayment(payment: Payment)
}