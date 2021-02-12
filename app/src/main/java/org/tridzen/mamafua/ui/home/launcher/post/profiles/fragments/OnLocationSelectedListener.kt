package org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments

import com.google.android.gms.maps.model.LatLng

interface OnLocationSelectedListener {

    fun onLocationSelected(location: LatLng)
}