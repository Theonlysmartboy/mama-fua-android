package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey
    val _id: String,
    val available: Boolean,
    val centerId: String,
    val email: String,
    val firstName: String,
    val imageUrl: String,
    val jobs: List<String>,
    val lastName: String,
    val rating: Float,
    val latitude: Double,
    val longitude: Double,
    val password: String,
    val phone: String,
    val status: String
)