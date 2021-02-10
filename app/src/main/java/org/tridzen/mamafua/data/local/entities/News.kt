package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class News(
    val __v: Int,
    @PrimaryKey
    val _id: String,
    val content: String,
    val createdAt: String,
    val imageUrl: String,
    val title: String,
    val updatedAt: String
)