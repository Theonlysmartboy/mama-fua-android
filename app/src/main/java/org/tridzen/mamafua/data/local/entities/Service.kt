package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Service(
    val __v: Int,
    @PrimaryKey
    val _id: String,
    val createdAt: String,
    val description: String,
    val imageUrl: String,
    val machinePrice: Int,
    val name: String,
    val offSitePrice: Int,
    val offerPc: Int,
    val onSitePrice: Int,
    val updatedAt: String
) {
    constructor() : this(
        __v = 0,
        _id = "",
        createdAt = "",
        description = "",
        imageUrl = "",
        machinePrice = 0,
        name = "",
        offSitePrice = 0,
        offerPc = 0,
        onSitePrice = 0,
        updatedAt = ""
    )
}