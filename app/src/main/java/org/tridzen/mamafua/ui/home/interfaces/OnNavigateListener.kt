package org.tridzen.mamafua.ui.home.interfaces

import com.google.android.gms.maps.model.LatLng
import org.tridzen.mamafua.data.local.entities.Profile

interface OnNavigateListener {

    fun onDateSelected(date: String)
    fun onProfileSelected(profile: Profile)
    fun onLocationSelected(latLng: LatLng)
    fun onNavigationRequest(page: Int)
}