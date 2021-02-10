package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tridzen.mamafua.utils.base.ExpandableAdapter

@Entity(tableName = "orders")
data class Order(
    var __v: Int = 0,
    @PrimaryKey
    var _id: String = "",
    var amount: Int = 0,
    var center: String = "",
    var createdAt: String = "",
    var deliveryDate: String = "",
    var fullfillerId: String = "",
    var fullfillerName: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var paidVia: String = "",
    var phone: String = "",
    var placedBy: String = "",
    var status: String = "",
    var transactionId: String = "",
    var updatedAt: String = "",
    var services: List<Cart> = listOf(),
) : ExpandableAdapter.ExpandableGroup<Cart>() {

    override fun getExpandingItems(): List<Cart> {
        val list: MutableList<Cart> = mutableListOf()
        for (i in this.services)
            list.add(i)
        return list
    }
}