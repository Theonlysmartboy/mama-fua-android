package org.tridzen.mamafua.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_PROFILE_ID = 0

@Entity
data class Profile(
    var _id: String,
    var available: Boolean,
    var centerId: String,
    var email: String,
    var firstName: String,
    var imageUrl: String,
    var jobs: List<String>,
    var lastName: String,
    var rating: Float,
    var latitude: Double,
    var longitude: Double,
    var password: String,
    var phone: String,
    var status: String
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_PROFILE_ID

    constructor() : this(
        "",
        false,
        "",
        "",
        "",
        "",
        emptyList(),
        lastName = "",
        0f,
        0.0,
        0.0,
        password = "",
        phone = "",
        status = ""
    )
}