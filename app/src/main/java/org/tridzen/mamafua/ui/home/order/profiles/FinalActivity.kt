package org.tridzen.mamafua.ui.home.order.profiles

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.ActivityProfilesBinding
import org.tridzen.mamafua.ui.home.interfaces.OnNavigateListener
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ViewPagerAdapter
import org.tridzen.mamafua.ui.home.order.profiles.fragments.DateTimeFragment
import org.tridzen.mamafua.ui.home.order.profiles.fragments.FilterFragment
import org.tridzen.mamafua.ui.home.order.profiles.fragments.LocationFragment
import org.tridzen.mamafua.ui.home.order.profiles.viewmodels.ProfilesViewModel
import org.tridzen.mamafua.utils.base.OnBottomSheetCallbacks
import javax.inject.Inject

@AndroidEntryPoint
class FinalActivity : AppCompatActivity(), OnNavigateListener {

    private var listener: OnBottomSheetCallbacks? = null
    val onNavigateListener: OnNavigateListener by lazy {
        this
    }

    private lateinit var binding: ActivityProfilesBinding

    @Inject
    lateinit var prefs: AppPreferences

    private val profilesViewModel by viewModels<ProfilesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing the shadow from the action bar
        supportActionBar?.elevation = 0f

        configureBackdrop()
        closeBottomSheet()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.vpProfiles.adapter = ViewPagerAdapter(
            this,
            arrayOf("", "", ""),
            arrayOf(FilterFragment(), DateTimeFragment(), LocationFragment())
        )
        binding.vpProfiles.isUserInputEnabled = false
    }

    fun setOnBottomSheetCallbacks(onBottomSheetCallbacks: OnBottomSheetCallbacks) {
        this.listener = onBottomSheetCallbacks
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    private fun configureBackdrop() {
        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        fragment?.view?.let {
            BottomSheetBehavior.from(it).let { bs ->
                bs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        listener?.onStateChanged(bottomSheet, newState)
                    }
                })

                bs.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                mBottomSheetBehavior = bs
            }
        }
    }

    override fun onDateSelected(date: String) {
        binding.vpProfiles.currentItem = 2
    }

    override fun onProfileSelected(profile: Profile) {
        binding.vpProfiles.currentItem = 1
        profilesViewModel.saveProfile(profile)
        closeBottomSheet()
    }

    override fun onLocationSelected(latLng: LatLng) {

    }

    override fun onNavigationRequest(page: Int) {
        binding.vpProfiles.currentItem = page
    }
}