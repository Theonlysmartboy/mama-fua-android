package org.tridzen.mamafua.ui.home.launcher.post

import org.tridzen.mamafua.data.local.entities.Order

interface OnOrderSelectedListener {
    fun onOrderSelected(order: Order)
}