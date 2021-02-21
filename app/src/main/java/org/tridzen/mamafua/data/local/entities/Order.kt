package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tridzen.mamafua.utils.base.ExpandableAdapter

@Entity(tableName = "orders")
data class Order(
    val __v: Int,
    @PrimaryKey
    val _id: String,
    val amount: Int,
    val center: String,
    val checkoutRequestId: String,
    val createdAt: String,
    val executionDate: String,
    val latitude: Double,
    val longitude: Double,
    val merchantRequestId: String,
    val paid: String,
    val paidVia: String,
    val phone: String,
    val placedBy: String,
    val profileId: String,
    val profileName: String,
    val services: List<CartX>,
    val status: String,
    val transactionId: String,
    val updatedAt: String
) : ExpandableAdapter.ExpandableGroup<CartX>() {

    override fun getExpandingItems(): List<CartX> {
        val list: MutableList<CartX> = mutableListOf()
        for (i in this.services)
            list.add(i)
        return list
    }

    data class Post(
        var amount: Int = 0,
        var center: String = "",
        var executionDate: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var status: String = "",
        var paidVia: String = "",
        var phone: String = "",
        var placedBy: String = "",
        var profileId: String = "",
        var profileName: String = "",
        var services: List<CartX> = listOf(),
    )
}