package org.tridzen.mamafua.ui.home.order.profiles.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.FragmentLocationBinding
import org.tridzen.mamafua.ui.home.interfaces.OnNavigateListener
import org.tridzen.mamafua.ui.home.order.OrderActivity
import org.tridzen.mamafua.ui.home.order.profiles.FinalActivity
import org.tridzen.mamafua.utils.coroutines.Coroutines
import java.util.*

@AndroidEntryPoint
class LocationFragment : Fragment(), OnNavigateListener {

    private val appPrefs by lazy {
        AppPreferences(requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        if (googleMap != null) {
            map = googleMap
        }

        map.setOnMapClickListener { point ->
            onLocationSelected(
                LatLng(
                    point.latitude,
                    point.longitude
                )
            )
        }
        map.isMyLocationEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val w: Window = requireActivity().window

        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), ACCESS_FINE_LOCATION
        )

        binding.butLocate.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onLocationSelected(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
                }
        }

        binding.butDone.setOnClickListener {
            startActivity(Intent(requireContext(), OrderActivity::class.java))
        }
    }

    override fun onDateSelected(date: String) {
    }

    override fun onProfileSelected(profile: Profile) {
    }

    override fun onLocationSelected(latLng: LatLng) {
        map.clear()
        map.addMarker(
            MarkerOptions().position(latLng).title("Set this as your current location")
        )
        val current = CameraUpdateFactory.newLatLngZoom(
            latLng, 15f
        )
        map.animateCamera(current)

        Coroutines.io {
            appPrefs.saveValue(latLng.latitude, AppPreferences.LATITUDE_PREFS)
            appPrefs.saveValue(
                latLng.longitude,
                AppPreferences.LONGITUDE_PREFS
            )
        }

        (activity as FinalActivity).onNavigateListener.onLocationSelected(latLng)
    }

    override fun onNavigationRequest(page: Int) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == ACCESS_FINE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            showDialog()
        }
    }

    private fun showDialog() {

    }

    private fun showConfirm() {
        MaterialDialog(requireContext()).show {
            customView(R.layout.dialog_confirm)
            val view = getCustomView()
            val confirm = view.butConfirm
            val cancel = view.butCancel
            val heading = view.tvWarning
            val content = view.tvMessage

            val title = "Select delivery mode."
            val message = "Choose between drop off and pick up modes."

            content.text = title
            heading.text = message

            confirm.setOnClickListener {
                dismiss()
            }

            cancel.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 2390
    }
}