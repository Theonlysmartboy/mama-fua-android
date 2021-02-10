package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.tridzen.mamafua.R

@Entity
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val number: String,
    val icon: Int = R.drawable.mpesa,
    val orders: List<String>
)
