package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val latitude: Double?,
    val longitude: Double?,
    val locationName: String?,
    val requesting: Boolean,
    val orders: List<Order>,
    val phone: String?,
    val points: Int,
    val updatedAt: String,
    val username: String,
    val password: String
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}