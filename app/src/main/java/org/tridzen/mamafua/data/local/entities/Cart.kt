package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey
    var id: String,
    var count: Int = 0,
    var service: Service,
    var style: String,
)


