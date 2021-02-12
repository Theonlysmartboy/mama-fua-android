package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentProfilesBinding
import org.tridzen.mamafua.utils.base.OnBottomSheetCallbacks
import org.tridzen.mamafua.utils.data.Prefs
import org.tridzen.mamafua.utils.runLayoutAnimation
import javax.inject.Inject

@AndroidEntryPoint
class ProfilesFragment : BottomSheetDialogFragment(), OnBottomSheetCallbacks {

    private val profilesViewModel by viewModels<ProfilesViewModel>()

    @Inject
    lateinit var prefs: Prefs
    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as FinalActivity).setOnBottomSheetCallbacks(this)
        _binding = FragmentProfilesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textResult.setOnClickListener {
            (activity as FinalActivity).openBottomSheet()
        }

        Log.d("State", currentState.toString())
        binding.filterImage.setOnClickListener {

            when (currentState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    (activity as FinalActivity).openBottomSheet()
                }
                else -> {
                    (activity as FinalActivity).closeBottomSheet()
                }
            }
        }

        profilesViewModel.getCenters()
        profilesViewModel.profilesByCenters.observe(requireActivity()) { resource ->
            when (resource) {
                is Resource.Failure -> {
                    Log.d("Profiles by center", "${resource.errorBody}")
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    setUpRecyclerView(resource.value.profiles)
                    Log.d("Profiles by center", "${resource.value.profiles.size}")
                }
            }
        }
    }

    private fun setUpRecyclerView(list: List<Profile>) = binding.rvProfiles.apply {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val profilesAdapter = ProfilesAdapter(list, prefs, findNavController())
        adapter = profilesAdapter
        runLayoutAnimation()
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        val scale = resources.displayMetrics.density
        val dpAsPixels = (24 * scale + 0.5f)
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                binding.textResult.text = "0 results"
                binding.filterImage.setImageResource(R.drawable.ic_baseline_filter_list_24)
                binding.cdlProfiles.setPadding(0, dpAsPixels.toInt(), 0, 0)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.textResult.text = "See the results"
                binding.filterImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                binding.cdlProfiles.setPadding(0, 0, 0, 0)
            }
        }
    }
}