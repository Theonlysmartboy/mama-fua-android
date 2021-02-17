package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.databinding.ActivityProfilesBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ViewPagerAdapter
import org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments.DateTimeFragment
import org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments.FilterFragment
import org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments.LocationFragment
import org.tridzen.mamafua.utils.base.OnBottomSheetCallbacks

@AndroidEntryPoint
class FinalActivity : AppCompatActivity() {

    private var listener: OnBottomSheetCallbacks? = null

    private lateinit var binding: ActivityProfilesBinding

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
}