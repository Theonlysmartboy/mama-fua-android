package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
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

    @Inject
    lateinit var prefs: Prefs
    private var currentState: Int = BottomSheetBehavior.STATE_HALF_EXPANDED

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!
    private var results: Int = 0

    private val profilesViewModel by activityViewModels<ProfilesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (currentState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    (activity as FinalActivity).closeBottomSheet()
                }

                else -> {
                    this.remove()
                    (activity as FinalActivity).onBackPressed()
                }
            }
        }
    }

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

        binding.filterImage.setOnClickListener {
            when (currentState) {
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    (activity as FinalActivity).openBottomSheet()
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    (activity as FinalActivity).closeBottomSheet()
                }
                else -> {
                    (activity as FinalActivity).openBottomSheet()
                }
            }
        }

        profilesViewModel.data.observe(activity as FinalActivity) {
            when (it) {
                is Resource.Success -> {
                    val show = if (it.value.profiles.isEmpty()) View.VISIBLE else View.GONE
                    binding.tvChoose.visibility = show
                    binding.lavChoose.visibility = show

                    setUpRecyclerView(it.value.profiles)
                    results = it.value.profiles.size
                }
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
            }
        }
    }

    private fun setUpRecyclerView(list: List<Profile>) {
        binding.rvProfiles.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ProfilesAdapter(list, prefs)
            runLayoutAnimation()
        }
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        val scale = resources.displayMetrics.density
        val dpAsPixels = (24 * scale + 0.5f)
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                (results.toString() + " " + getString(R.string.results_in_estate)).also {
                    binding.textResult.text = it
                }
                binding.filterImage.setImageResource(R.drawable.ic_filter)
                binding.cdlProfiles.setPadding(0, dpAsPixels.toInt(), 0, 0)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.textResult.text = getString(R.string.see_results)
                binding.filterImage.setImageResource(R.drawable.ic_up_arrow)
                binding.cdlProfiles.setPadding(0, 0, 0, 0)
            }
        }
    }
}