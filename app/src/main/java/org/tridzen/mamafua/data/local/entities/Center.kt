package org.tridzen.mamafua.data.local.entities

import com.google.android.gms.maps.model.LatLng

data class Center(
    val _id: String,
    val name: String,
    val phone: String,
    val location: LatLng,
    val status: String,
    val providers: List<String>,
)